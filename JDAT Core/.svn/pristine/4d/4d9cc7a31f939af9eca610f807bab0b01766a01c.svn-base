package cn.edu.buaa.sei.jdat.graph;

import java.util.HashMap;
import java.util.Map;

import cn.edu.buaa.sei.jdat.model.Jar;
import cn.edu.buaa.sei.jdat.model.Package;

public class BlendDependency implements IDependency<Jar, Package> {

	private Jar source;
	private Package destination;
	
	@Override
	public Jar getSource() {
		return source;
	}

	@Override
	public Package getDestination() {
		return destination;
	}
	
	private Map<String, PackageDependency> details;
	
	protected void addDependency(PackageDependency dependency) {
		if (details == null)
			details = new HashMap<String, PackageDependency>();
		details.put(DependencyGraph.getKey(dependency.getSource(), dependency.getDestination()), dependency);
	}
	
	public PackageDependency[] getDependencies() {
		if (details == null)
			return null;
		return details.values().toArray(new PackageDependency[0]);
	}
	
	public PackageDependency getDependency(Package pkg, Package refer) {
		String key = DependencyGraph.getKey(pkg, refer);
		return details.get(key);
	}
	
	public BlendDependency(Jar source, Package dest) {
		this.source = source;
		this.destination = dest;
	}

}
