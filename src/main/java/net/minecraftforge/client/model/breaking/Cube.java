package net.minecraftforge.client.model.breaking;

import javax.vecmath.Vector3f;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.EnumFaceDirection.Constants;
import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
import net.minecraft.util.EnumFacing;

public class Cube
{
	private Edge[] edges = new Edge[12];
	private Vector3f[] corners = new Vector3f[8];
	private Vector3f minCorner = new Vector3f(0, 0, 0);
	private Vector3f maxCorner = new Vector3f(1, 1, 1);
	
	public Cube()
	{
		this.makeCornerVerts();
	}
	
	public Cube(Vector3f from, Vector3f to)
	{
		this.minCorner = new Vector3f(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		this.maxCorner = new Vector3f(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		this.makeCornerVerts();
	}
	
	private float[] getPositions()
	{
		float[] ret = new float[EnumFacing.values().length];
		ret[Constants.WEST_INDEX] = this.minCorner.x;
		ret[Constants.DOWN_INDEX] = this.minCorner.y;
		ret[Constants.NORTH_INDEX] = this.minCorner.z;
		ret[Constants.EAST_INDEX] = this.maxCorner.x;
		ret[Constants.UP_INDEX] = this.maxCorner.y;
		ret[Constants.SOUTH_INDEX] = this.maxCorner.z;
		return ret;
	}
	
	private void makeCornerVerts()
	{
		float[] positions = this.getPositions();
		int vert = 0;
		for (EnumFacing face : EnumFacing.Plane.VERTICAL.facings())
		{
			for (int i = 0; i < 4; i++)
			{
				VertexInformation vertInfo = EnumFaceDirection.getFacing(face).func_179025_a(i);
				this.corners[vert] = new Vector3f(positions[vertInfo.field_179184_a], positions[vertInfo.field_179182_b], positions[vertInfo.field_179183_c]);
				vert++;
			}
		}
	}
	
	private void makeEdges()
	{
		Vector3f[] topCorners = ArrayUtils.subarray(this.corners, 0, 4);
		Vector3f[] bottomCorners = ArrayUtils.subarray(this.corners, 4, 8);
		for (int i = 3; i >= 0; i++)
		{
			
		}
	}
}
