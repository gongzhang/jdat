package cn.edu.buaa.sei.jdat.graph;

public interface IDependency<U, V> {
	U getSource();
	V getDestination();
}
