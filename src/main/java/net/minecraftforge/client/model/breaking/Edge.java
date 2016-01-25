package net.minecraftforge.client.model.breaking;

import java.util.Arrays;

import javax.vecmath.Vector3f;

public class Edge
{
	private Vector3f[] verts = new Vector3f[] {new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)};
	private int numSegments = 1;
	
	public Edge()
	{
		this(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
	}
	
	public Edge(Vector3f a, Vector3f b)
	{
		this(a, b, 1);
	}
	
	public Edge(Vector3f a, Vector3f b, int numSegments)
	{
		this.numSegments = numSegments;
		this.verts = this.getSegmentedVertices(a, b, numSegments);
	}
	
	private Vector3f[] getSegmentedVertices(Vector3f a, Vector3f b, int numSegments)
	{
		if (numSegments < 1) return null;
		Vector3f[] ret = new Vector3f[numSegments + 1];
		ret[0] = a;
		ret[numSegments] = b;
		for (int i = 1; i < numSegments; i++)
		{
			float r = (float) i / (float) numSegments;
			Vector3f ar = new Vector3f();
			Vector3f br = new Vector3f();
			ar.scale(1.0f - r, a);
			br.scale(r, b);
			ret[i] = new Vector3f();
			ret[i].add(ar, br);
		}
		return ret;
	}
	
	public void setNumSegments(int numSegments)
	{
		if (numSegments < 1) return;
		this.verts = this.getSegmentedVertices(this.verts[0], this.verts[this.numSegments], numSegments);
		this.numSegments = numSegments;
	}
	
	public int getNumSegments()
	{
		return this.numSegments;
	}
	
	public int getNumVerts()
	{
		return this.verts.length;
	}
	
	public Vector3f[] getVertices()
	{
		return this.verts;
	}
	
	public float totalLength()
	{
		Vector3f sum = new Vector3f();
		sum.add(this.verts[0], this.verts[this.numSegments]);
		return sum.length();
	}
	
	public String vertsToString()
	{
		return Arrays.toString(this.verts);
	}
	
	@Override
	public String toString()
	{
		return String.format("segments: %d, totalLength: %d, vertices: %s", this.numSegments, this.totalLength(), Arrays.toString(this.verts));
	}
}
