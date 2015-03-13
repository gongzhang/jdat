package cn.edu.buaa.sei.jdat.vt.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import cn.edu.buaa.sei.jdat.graph.DependencyGraph;
import cn.edu.buaa.sei.jdat.graph.JarDependency;
import cn.edu.buaa.sei.jdat.graph.PackageDependency;
import cn.edu.buaa.sei.jdat.graph.TypeDependency;
import cn.edu.buaa.sei.jdat.model.Class;
import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.model.Type;
import cn.edu.buaa.sei.jdat.vt.ErrorDialog;

public abstract class GraphReport {
	private Map<String, String> replaceTokens = new HashMap<String, String>();
	private String[] indexFragments;
	private String[] jarDependFragments;
	private String folderName = "jdat report";
	
	public GraphReport() {
		parseReplace();
		loadFragments();
	}
	
	public abstract void generateGraphImage(File file);
	
	public void generateReport(DependencyGraph graph, File dir) {
		if (!dir.isDirectory()) {
			ErrorDialog.showErrorDialog(new IllegalArgumentException("argument dir should be a directory"));
			return;
		}
		File folder = createFolderAndOrnaments(dir);
		
		try {
			FileWriter indexWriter = new FileWriter(new File(folder, "index.html"));
			indexWriter.write(indexFragments[0]);
			// Internal Jars Info.
			String format = replaceTokens.get("JarInfo");
			for (Jar jar : graph.getInternalJars()) {
				indexWriter.write(String.format(format, jar.getName(), jar.getURI()));
			}
			indexWriter.write(indexFragments[1]);
			//External Jars Info.
			if (graph.isOptionFlagged(DependencyGraph.OPT_EXTERNAL_JARS)) {
				for (Jar jar : graph.getExternalJars()) {
					indexWriter.write(String.format(format, jar.getName(), jar.getURI()));
				}
			}
			indexWriter.write(indexFragments[2]);
			//Mark Dependencies with Number
			HashMap<JarDependency, String> dependOrders = new HashMap<JarDependency, String>();
			int count = 0;
			for (JarDependency jdep : graph.getInternalDependencies()) {
				dependOrders.put(jdep, Integer.toHexString(count++));
			}
			if (graph.isOptionFlagged(DependencyGraph.OPT_EXTERNAL_JARS) || graph.isOptionFlagged(DependencyGraph.OPT_UNKNOWN_PACKAGES)) {
				for (JarDependency jdep : graph.getExternalDependencies()) {
					dependOrders.put(jdep, Integer.toHexString(count++));
				}
			}
			//Dependencies
			String itemFmt = replaceTokens.get("JarDependItem");
			String eitemFmt = replaceTokens.get("JarDependItemE");
			String uitemFmt = replaceTokens.get("JarDependItemU");
			format = replaceTokens.get("JarDependRow");
			for (Jar jar : graph.getInternalJars()) {
				StringBuffer refers = new StringBuffer();
				StringBuffer refees = new StringBuffer();
				for (JarDependency jd : dependOrders.keySet()) {
					if (jar == jd.getSource()) {
						if (jd.getDestination().isUnknown())
							refers.append(String.format(uitemFmt, getDetailPageName(dependOrders.get(jd)), "Unknown Jar(s)"));
						else if (graph.isExternal(jd.getDestination()))
							refers.append(String.format(eitemFmt, getDetailPageName(dependOrders.get(jd)), jd.getDestination().getName()));
						else
							refers.append(String.format(itemFmt, getDetailPageName(dependOrders.get(jd)), jd.getDestination().getName()));
					}
					else if (jar == jd.getDestination()) {
						if (graph.isExternal(jd.getSource()))
							refees.append(String.format(eitemFmt, getDetailPageName(dependOrders.get(jd)), jd.getSource().getName()));
						else
							refees.append(String.format(itemFmt, getDetailPageName(dependOrders.get(jd)), jd.getSource().getName()));
					}
				}
				indexWriter.write(String.format(format, jar.getName(), refers.toString(), refees.toString()));
			}
			indexWriter.write(indexFragments[3]);
			indexWriter.close();
			// jdep files
			for (JarDependency jd : dependOrders.keySet()) {
				FileWriter jdepWriter = new FileWriter(new File(folder, getDetailPageName(dependOrders.get(jd))));
				jdepWriter.write(jarDependFragments[0]);
				//Detail FROM Jar Title
				format = replaceTokens.get("JarRefTitle");
				jdepWriter.write(String.format(format, jd.getSource().getName(), jd.getSource().getURI()));
				jdepWriter.write(jarDependFragments[1]);
				//Detail TO Jar Title
				if (jd.getDestination().isUnknown())
					jdepWriter.write(String.format(format, "Unknown Jar(s)", "All unknown packages are considered in this Jar."));
				else
					jdepWriter.write(String.format(format, jd.getDestination().getName(), jd.getDestination().getURI()));
				jdepWriter.write(jarDependFragments[2]);
				//Package Dependencies Number
				format = replaceTokens.get("PackageDependNumber");
				jdepWriter.write(String.format(format, jd.getDependencies().length));
				//Package Dependencies Details
				for (PackageDependency pd : jd.getDependencies()) {
					{
						//Package Dependency
						String pkgName = replaceTokens.get("PackageName");
						String upkgName = replaceTokens.get("UnknownPkgName");
						format = replaceTokens.get("PackageDepend");
						String dest = null;
						if (pd.getDestination().isUnknown())
							dest = String.format(upkgName, pd.getDestination().getName());
						else
							dest = String.format(pkgName, pd.getDestination().getName());
						jdepWriter.write(String.format(format, pd.getSource().getName(), dest));
					}
					//Type details
					HashMap<Type, StringBuffer> typeDeps = new HashMap<Type, StringBuffer>();
					for (TypeDependency tp : pd.getDependencies(graph)) {
						StringBuffer typeList = typeDeps.get(tp.getSource());
						if (typeList == null) {
							typeList = new StringBuffer();
							typeDeps.put(tp.getSource(), typeList);
						} else {
							typeList.append(',').append(' ');
						}
						String typeFormat = null;
						if (tp.getDestination().isUnknown()) {
							if (tp.getKind() == TypeDependency.INHERITANCE)
								typeFormat = replaceTokens.get("UnknownTypeNameS");
							else
								typeFormat = replaceTokens.get("UnknownTypeName");
						} else if (tp.getDestination() instanceof Class) {
							if (tp.getKind() == TypeDependency.INHERITANCE)
								typeFormat = replaceTokens.get("ClassNameS");
							else
								typeFormat = replaceTokens.get("ClassName");
						} else /*if (tp.getDestination() instanceof Interface)*/ {
							if (tp.getKind() == TypeDependency.INHERITANCE)
								typeFormat = replaceTokens.get("InterfaceNameS");
							else
								typeFormat = replaceTokens.get("InterfaceName");
						}
						typeList.append(String.format(typeFormat, tp.getDestination().getName()));
					}
					//Combine into Rows.
					StringBuffer rows = new StringBuffer();
					{
						String rowFormat = replaceTokens.get("ClassDepend");
						String claFormat = replaceTokens.get("ClassName");
						String intFormat = replaceTokens.get("InterfaceName");
						String enuFormat = replaceTokens.get("EnumName");
						for (Type type : typeDeps.keySet()) {
							String typeName = null;
							if (type instanceof Class) {
								if (((Class) type).getSuperclassName() == "java.lang.Enum")
									typeName = String.format(enuFormat, type.getName());
								else
									typeName = String.format(claFormat, type.getName());
							} else
								typeName = String.format(intFormat, type.getName());
							rows.append(String.format(rowFormat, typeName, typeDeps.get(type).toString()));
						}
					}
					//Wrap to a table.
					format = replaceTokens.get("ClassDependTable");
					jdepWriter.append(String.format(format, rows.toString()));
				}
				jdepWriter.append(jarDependFragments[3]);
				jdepWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getDetailPageName(String number) {
		return "jdep_" + number + ".html";
	}
	
	private File createFolderAndOrnaments(File dir) {
		File folder = new File(dir, folderName);
		folder.mkdir();
		
		File imagesFolder = new File(folder, "images");
		imagesFolder.mkdir();
		createFile("arrow.png", imagesFolder);
		createFile("icon_class.png", imagesFolder);
		createFile("icon_enum.png", imagesFolder);
		createFile("icon_interface.png", imagesFolder);
		createFile("icon_jar.png", imagesFolder);
		createFile("icon_package.png", imagesFolder);
		createFile("icon_unknown_jar.png", imagesFolder);
		createFile("icon_unknown_pkg.png", imagesFolder);
		createFile("icon_unknown_type.png", imagesFolder);
		generateGraphImage(new File(imagesFolder, "graph.jpg"));
		
		File styleFolder = new File(folder, "style");
		styleFolder.mkdir();
		createFile("main.css", styleFolder);
		
		return folder;
	}
	
	private void createFile(String innerFileName, File dir) {
		InputStream is = GraphReport.class.getResourceAsStream("res/" + innerFileName);
		try {
			FileOutputStream fos = new FileOutputStream(new File(dir, innerFileName));
			byte[] buf = new byte[1024];
			int read = 0;
			while ((read = is.read(buf)) != -1) {
				fos.write(buf, 0, read);
			}
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseReplace() {
		try {
			InputStreamReader isr = new InputStreamReader(GraphReport.class.getResourceAsStream("res/replace.txt"));
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				replaceTokens.put(line.substring(0, line.indexOf(':')), line.substring(line.indexOf(':') + 1));
			}
			isr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadFragments() {
		indexFragments = new String[4];
		for (int i = 0; i < 4; i++) {
			indexFragments[i] = readAll(GraphReport.class.getResourceAsStream(String.format("res/index_%d.f", i+1)));
		}
		jarDependFragments = new String[4];
		for (int i = 0; i < 4; i++) {
			jarDependFragments[i] = readAll(GraphReport.class.getResourceAsStream(String.format("res/jard_%d.f", i+1)));
		}
	}
	
	private String readAll(InputStream stream) {
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String line = "";
		StringBuffer sb = new StringBuffer();
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
