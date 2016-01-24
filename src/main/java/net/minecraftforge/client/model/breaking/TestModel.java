package net.minecraftforge.client.model.breaking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
import net.minecraft.client.renderer.EnumFaceDirection.Constants;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.common.FMLLog;

public class TestModel implements IBakedModel
{
	private TextureAtlasSprite sprite;
	private List<BakedQuad> generalQuads = Collections.emptyList();
	private EnumMap<EnumFacing, List<BakedQuad>> faceQuads = Maps.<EnumFacing, List<BakedQuad>>newEnumMap(EnumFacing.class);
	private static final EnumMap<EnumFacing, List<Vector3f>> unitQuads = Maps.<EnumFacing, List<Vector3f>>newEnumMap(EnumFacing.class);
	private static final Vector2f[] uvs = new Vector2f[4];
	private boolean hasMadeFaces = false;
	
	private Vector3f minCorner = new Vector3f(0, 0, 0);
	private Vector3f maxCorner = new Vector3f(1, 1, 1);
	private Vector3f[] corners = new Vector3f[8];
//	private EnumMap<EnumFacing.Axis, Edge[]> edgeMap = Maps.<EnumFacing.Axis, Edge[]>newEnumMap(EnumFacing.Axis.class);
	private Map<Pair<EnumFacing.Axis, EnumFacing>, Edge[]> edgeMap = Maps.<Pair<EnumFacing.Axis, EnumFacing>, Edge[]>newHashMap();
	
	static
	{
		uvs[0] = new Vector2f(0, 16);
		uvs[1] = new Vector2f(16, 16);
		uvs[2] = new Vector2f(16, 0);
		uvs[3] = new Vector2f(0, 0);
		
		Vector3f[] vectors = new Vector3f[]
		{
			new Vector3f(0, 0, 1),
			new Vector3f(1, 0, 1),
			new Vector3f(0, 1, 1),
			new Vector3f(1, 1, 1),
			new Vector3f(0, 1, 0),
			new Vector3f(1, 1, 0),
			new Vector3f(0, 0, 0),
			new Vector3f(1, 0, 0)
		};
		
		unitQuads.put(EnumFacing.DOWN, Lists.newArrayList(vectors[6], vectors[7], vectors[1], vectors[0]));
		unitQuads.put(EnumFacing.UP, Lists.newArrayList(vectors[2], vectors[3], vectors[5], vectors[4]));
		unitQuads.put(EnumFacing.NORTH, Lists.newArrayList(vectors[4], vectors[5], vectors[7], vectors[6]));
		unitQuads.put(EnumFacing.SOUTH, Lists.newArrayList(vectors[0], vectors[1], vectors[3], vectors[2]));
		unitQuads.put(EnumFacing.WEST, Lists.newArrayList(vectors[6], vectors[0], vectors[2], vectors[4]));
		unitQuads.put(EnumFacing.EAST, Lists.newArrayList(vectors[1], vectors[7], vectors[5], vectors[3]));
	}
	
	public TestModel(TextureAtlasSprite sprite)
	{
		this.sprite = sprite;
		this.makeFaces();
	}
	
	public TestModel(Vector3f from, Vector3f to, TextureAtlasSprite sprite)
	{
		this.sprite = sprite;
		this.minCorner = new Vector3f(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z));
		this.maxCorner = new Vector3f(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z));
		this.makeCorners();
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
		for (EnumFacing.Axis axis : EnumFacing.Axis.values())
		{
			EnumFacing axisFace = EnumFacing.func_181076_a(EnumFacing.AxisDirection.NEGATIVE, axis);
			Edge[] edges = new Edge[4];
			for (int i = 0; i < 4; i++)
			{
				
			}
		}
	}
	
	private void makeFaces()
	{
		for (EnumFacing face : EnumFacing.values())
		{
			List<Vector3f> units = unitQuads.get(face);
			List<BakedQuad> quads = Lists.newArrayListWithCapacity(9);
			float d = LightUtil.diffuseLight(face);
			UnpackedBakedQuad.Builder builder;
			
			for (int r = -1; r < 2; r++)
			{
				for (int c = -1; c < 2; c++)
				{
					builder = new UnpackedBakedQuad.Builder(Attributes.DEFAULT_BAKED_FORMAT);
					builder.setQuadOrientation(face);
					for (int i = 0; i < 4; i++)
					{
						Vector3f unit = new Vector3f(units.get(i));
						switch (face)
						{
						case DOWN:
						case UP:
							unit.x += (float) c;
							unit.y += face.getAxisDirection().getOffset();
							unit.z += (float) r;
							break;
						case NORTH:
						case SOUTH:
							unit.x += (float) c;
							unit.y += (float) r;
							unit.z += face.getAxisDirection().getOffset();
							break;
						case WEST:
						case EAST:
							unit.x += face.getAxisDirection().getOffset();
							unit.y += (float) r;
							unit.z += (float) c;
						}
						builder.put(0, unit.x, unit.y, unit.z, 1.0f);
						builder.put(1, d, d, d, 1);
						builder.put(2, this.sprite.getInterpolatedU(uvs[i].x), this.sprite.getInterpolatedV(uvs[i].y), 0, 1);
						builder.put(3);
					}
					quads.add(builder.build());
				}
			}
			this.faceQuads.put(face, quads);
		}
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing face)
	{
		if (!this.hasMadeFaces) this.makeFaces();
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
