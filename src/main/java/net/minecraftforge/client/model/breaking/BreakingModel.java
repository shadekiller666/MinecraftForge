package net.minecraftforge.client.model.breaking;

import java.util.Arrays;
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
import net.minecraft.util.EnumFacing.Plane;
import net.minecraftforge.fml.common.FMLLog;

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
	private Map<VertexInformation, Vector3f> corners = Maps.<VertexInformation, Vector3f>newHashMapWithExpectedSize(8);
	private Map<Pair<EnumFacing, EnumFacing>, Edge> edges = Maps.<Pair<EnumFacing, EnumFacing>, Edge>newHashMapWithExpectedSize(12);
	
	public BreakingModel(Vector3f from, Vector3f to, TextureAtlasSprite sprite)
	{
		this.sprite = sprite;
		this.minCorner = new Vector3f(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		this.maxCorner = new Vector3f(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		this.makeCorners();
		FMLLog.info("");
		this.makeEdges();
		FMLLog.info("");
		this.makeFaces();
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
		for (Map.Entry<VertexInformation, Vector3f> e : this.corners.entrySet())
		{
			FMLLog.info("%s: %s", e.getKey(), e.getValue());
		}
	}
	
	private void makeEdges()
	{
		this.edges.clear();
		for (Axis axis : Axis.values())
		{
			EnumFaceDirection negDir = EnumFaceDirection.getFacing(EnumFacing.func_181076_a(AxisDirection.NEGATIVE, axis));
			EnumFaceDirection posDir = EnumFaceDirection.getFacing(EnumFacing.func_181076_a(AxisDirection.POSITIVE, axis));
			for (int i = 0; i < 4; i++)
			{
				EnumFacing xFace, yFace, zFace;
				switch (axis)
				{
				case X:
					yFace = EnumFacing.values()[negDir.func_179025_a(i).field_179182_b];
					zFace = EnumFacing.values()[negDir.func_179025_a(i).field_179183_c];
					this.edges.put(Pair.of(yFace, zFace), new Edge(this.corners.get(negDir.func_179025_a(i)), this.corners.get(posDir.func_179025_a(3 - i))));
					break;
				case Y:
					xFace = EnumFacing.values()[negDir.func_179025_a(i).field_179184_a];
					zFace = EnumFacing.values()[negDir.func_179025_a(i).field_179183_c];
					this.edges.put(Pair.of(xFace, zFace), new Edge(this.corners.get(negDir.func_179025_a(i)), this.corners.get(i < 2 ? posDir.func_179025_a(1 - i) : posDir.func_179025_a((3 - i) + 2))));
					break;
				case Z:
					xFace = EnumFacing.values()[negDir.func_179025_a(i).field_179184_a];
					yFace = EnumFacing.values()[negDir.func_179025_a(i).field_179182_b];
					this.edges.put(Pair.of(xFace, yFace), new Edge(this.corners.get(negDir.func_179025_a(i)), this.corners.get(posDir.func_179025_a(3 - i))));
					break;
				}
			}
		}
		for (Map.Entry<Pair<EnumFacing, EnumFacing>, Edge> e : this.edges.entrySet())
		{
			FMLLog.info("(%s, %s): %s", e.getKey().getLeft(), e.getKey().getRight(), e.getValue().vertsToString());
		}
	}
	
	public Edge getEdge(EnumFacing a, EnumFacing b)
	{
		if (this.edges.containsKey(Pair.of(a, b)))
		{
			return this.edges.get(Pair.of(a, b));
		}
		else if (this.edges.containsKey(Pair.of(b, a)))
		{
			return this.edges.get(Pair.of(b, a));
		}
		return null;
	}
	
	private void makeFaces()
	{
		EnumMap<EnumFacing, Edge[]> unbakedFaces = Maps.newEnumMap(EnumFacing.class);
		for (EnumFacing face : EnumFacing.values())
		{
			Edge[] edges = new Edge[4];
			EnumFacing[] adjacents = new EnumFacing[4];
			switch (face.getAxis())
			{
			case X:
				adjacents = new EnumFacing[] {face.rotateY(), face.rotateY().rotateX(), face.rotateYCCW(), face.rotateYCCW().rotateX()};
				break;
			case Y:
				adjacents = Plane.HORIZONTAL.facings();
				break;
			case Z:
				adjacents = new EnumFacing[] {face.rotateY(), face.rotateY().rotateZ(), face.rotateYCCW(), face.rotateYCCW().rotateZ()};
				break;
			}
			for (int i = 0; i < 4; i++)
			{
				edges[i] = this.getEdge(face, adjacents[i]);
				if (i > 1)
				{
					if (!edges[i].getStartVertex().equals(edges[i - 1].getEndVertex()))
					{
						edges[i].reverse();
					}
				}
				FMLLog.info("(%s, %s): %s", face, adjacents[i], edges[i].vertsToString());
			}
			unbakedFaces.put(face, edges);
		}
	}
	
	private void bakeFaces(EnumMap<EnumFacing, Edge[]> unbakedFaces)
	{
		for (Map.Entry<EnumFacing, Edge[]> e : unbakedFaces.entrySet())
		{
			List<BakedQuad> quads = Lists.newArrayList();
			EnumFacing face = e.getKey();
			Edge[] edges = e.getValue();
			
		}
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face)
	{
		return Collections.emptyList();
//		return this.faceQuads.get(face);
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
