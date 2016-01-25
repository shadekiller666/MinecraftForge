package net.minecraftforge.client.model.breaking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.EnumFaceDirection.Constants;
import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;

public class BreakingModel implements IBakedModel
{
	private TextureAtlasSprite sprite;
	private List<BakedQuad> generalQuads = Collections.emptyList();
	private EnumMap<EnumFacing, List<BakedQuad>> faceQuads = Maps.<EnumFacing, List<BakedQuad>>newEnumMap(EnumFacing.class);
	private static final EnumMap<EnumFacing, List<Vector3f>> unitQuads = Maps.<EnumFacing, List<Vector3f>>newEnumMap(EnumFacing.class);
	private static final Vector2f[] uvs = new Vector2f[4];
	private boolean hasMadeFaces = false;
	
	private Vector3f minCorner = new Vector3f(0, 0, 0);
	private Vector3f maxCorner = new Vector3f(1, 1, 1);
//	private Vector3f[] corners = new Vector3f[8];
	private Map<VertexInformation, Vector3f> corners = Maps.<VertexInformation, Vector3f>newHashMapWithExpectedSize(8);
	private Map<Pair<EnumFacing, EnumFacing>, Edge> edges = Maps.<Pair<EnumFacing, EnumFacing>, Edge>newHashMapWithExpectedSize(12);
	
	public BreakingModel(Vector3f from, Vector3f to, TextureAtlasSprite sprite)
	{
		this.sprite = sprite;
		this.minCorner = new Vector3f(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		this.maxCorner = new Vector3f(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		
	}
	
	private float[] getCornerPositions()
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
	
	private void makeCorners()
	{
		float[] positions = this.getCornerPositions();
		for (EnumFacing face : EnumFacing.Plane.VERTICAL.facings())
		{
			for (int i = 0; i < 4; i++)
			{
				VertexInformation info = EnumFaceDirection.getFacing(face).func_179025_a(i);
				Vector3f coords = new Vector3f(positions[info.field_179184_a], positions[info.field_179182_b], positions[info.field_179183_c]);
				this.corners.put(info, coords);
			}
		}
	}
	
	private void makeEdges()
	{
		for (Axis axis : Axis.values())
		{
			EnumFacing pos = EnumFacing.func_181076_a(AxisDirection.NEGATIVE, axis);
			EnumFacing neg = EnumFacing.func_181076_a(AxisDirection.POSITIVE, axis);
			EnumFaceDirection posDir = EnumFaceDirection.getFacing(pos);
			EnumFaceDirection negDir = EnumFaceDirection.getFacing(neg);
			int j = 3;
			for (int i = 0; i < 4; i++)
			{
				VertexInformation negInfo = negDir.func_179025_a(i);
				VertexInformation posInfo = posDir.func_179025_a(j);
				switch (axis)
				{
				case X:
					EnumFacing yFace = EnumFacing.values()[negInfo.field_179182_b];
					EnumFacing zFace = EnumFacing.values()[negInfo.field_179183_c];
					this.edges.put(Pair.of(yFace, zFace), new Edge(this.corners.get(negInfo), this.corners.get(posInfo)));
					break;
				case Y:
					break;
				case Z:
					break;
				}
			}
		}
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face)
	{
		return this.faceQuads.get(face);
	}

	@Override
	public List<BakedQuad> getGeneralQuads()
	{
		return this.generalQuads;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return true;
	}

	@Override
	public boolean isGui3d()
	{
		return true;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return this.sprite;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms()
	{
		return ItemCameraTransforms.DEFAULT;
	}
}
