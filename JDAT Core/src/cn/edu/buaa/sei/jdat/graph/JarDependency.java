package cn.edu.buaa.sei.jdat.graph;

import java.util.HashMap;
import java.util.Map;

import cn.edu.buaa.sei.jdat.model.Package;

import cn.edu.buaa.sei.jdat.model.Jar;

public class JarDependency implements IDependency<Jar, Jar> {

	private Jar source;
	private Jar destination;
	
	private Map<String, PackageDependency> details;
	
	@Override
	public Jar getSource() {
		return source;
	}
	
	@Override
	public Jar getDestination() {
		return destination;
	}
	
	private void generateDetails() {
		if (details == null) {
			details = new HashMap<String, PackageDependency>();
			for (Package pkg : source.getPackages()) {
				for (Package refer : pkg.getReferences()) {
					if (refer.getOwner() == destination) {
						details.put(DependencyGraph.getKey(pkg, refer), new PackageDependency(pkg, refer));
					}
				}
			}
		}
	}
	
	public PackageDependency[] getDependencies() {
		if (details == null)
			generateDetails();
		return details.values().toArray(new PackageDependency[0]);
	}
	
	public int getDependenciesCount() {
		return details == null ? 0 : details.size();
	}
	
	public PackageDependency getDependency(Package pkg, Package refer) {
		if (details == null)
			generateDetails();
		String key = DependencyGraph.getKey(pkg, refer);
		return details.get(key);
	}
	
	public JarDependency(Jar source, Jar dest) {
		this.source = source;
		this.destination = dest;
	}
	
	protected void addDependency(PackageDependency dependency) {
		if (details == null)
			details = new HashMap<String, PackageDependency>();
		details.put(DependencyGraph.getKey(dependency.getSource(), dependency.getDestination()), dependency);
	}
}
