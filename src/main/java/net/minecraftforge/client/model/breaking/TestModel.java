package net.minecraftforge.client.model.breaking;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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

public class TestModel implements IBakedModel
{
	private TextureAtlasSprite sprite;
	private List<BakedQuad> generalQuads = Collections.emptyList();
	private EnumMap<EnumFacing, List<BakedQuad>> faceQuads = Maps.<EnumFacing, List<BakedQuad>>newEnumMap(EnumFacing.class);
	private static final EnumMap<EnumFacing, List<Vector3f>> unitQuads = Maps.<EnumFacing, List<Vector3f>>newEnumMap(EnumFacing.class);
	private static final Vector2f[] uvs = new Vector2f[4];
	
	static
	{
		uvs[0] = new Vector2f(0.0f, 16.0f);
		uvs[1] = new Vector2f(16.0f, 16.0f);
		uvs[2] = new Vector2f(16.0f, 0.0f);
		uvs[3] = new Vector2f(0.0f, 0.0f);
		
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
	}
	
	private void generate()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(Attributes.DEFAULT_BAKED_FORMAT);
		for (EnumFacing facing : EnumFacing.values())
		{
			builder.setQuadOrientation(facing);
			switch (facing)
			{
			case DOWN:
				
				break;
			case UP:
				break;
			case NORTH:
				break;
			case SOUTH:
				break;
			case WEST:
				break;
			case EAST:
				break;
			}
		}
	}
	
	private void xAxis(EnumFacing facing)
	{
		if (facing.getAxis() != EnumFacing.Axis.X) return;
		List<Vector3f> units = unitQuads.get(facing);
		List<BakedQuad> quads = Lists.newArrayListWithCapacity(9);
		float d = LightUtil.diffuseLight(facing);
		UnpackedBakedQuad.Builder builder;
		for (int r = -1; r < 2; r++)
		{
			for (int c = -1; c < 2; c++)
			{
				builder = new UnpackedBakedQuad.Builder(Attributes.DEFAULT_BAKED_FORMAT);
				for (int i = 0; i < 4; i++)
				{
					Vector3f unit = units.get(i);
					builder.setQuadOrientation(facing);
					builder.put(0, unit.x + (float) facing.getAxisDirection().getOffset(), unit.y, unit.z, 1.0f);
					builder.put(1, d, d, d, 1);
					builder.put(2, this.sprite.getInterpolatedU(uvs[i].x), this.sprite.getInterpolatedV(uvs[i].y), 0, 1);
					builder.put(3);
				}
				quads.add(builder.build());
			}
		}
		this.faceQuads.put(facing, quads);
	}
	
	private void yAxis(EnumFacing facing)
	{
		if (facing.getAxis() != EnumFacing.Axis.Y) return;
		List<Vector3f> units = unitQuads.get(facing);
		List<BakedQuad> quads = Lists.newArrayListWithCapacity(9);
		float d = LightUtil.diffuseLight(facing);
		UnpackedBakedQuad.Builder builder;
		for (int r = -1; r < 2; r++)
		{
			for (int c = -1; c < 2; c++)
			{
				builder = new UnpackedBakedQuad.Builder(Attributes.DEFAULT_BAKED_FORMAT);
				for (int i = 0; i < 4; i++)
				{
					Vector3f unit = units.get(i);
					builder.setQuadOrientation(facing);
					builder.put(0, unit.x + (float) c, unit.y + (float) facing.getAxisDirection().getOffset(), unit.z + (float) r, 1.0f);
					builder.put(1, d, d, d, 1);
					builder.put(2, this.sprite.getInterpolatedU(uvs[i].x), this.sprite.getInterpolatedV(uvs[i].y), 0, 1);
					builder.put(3);
				}
				quads.add(builder.build());
			}
		}
		this.faceQuads.put(facing, quads);
	}
	
	private void zAxis(EnumFacing facing)
	{
		if (facing.getAxis() != EnumFacing.Axis.Z) return;
		List<Vector3f> units = unitQuads.get(facing);
		List<BakedQuad> quads = Lists.newArrayListWithCapacity(9);
		float d = LightUtil.diffuseLight(facing);
		UnpackedBakedQuad.Builder builder;
		for (int r = -1; r < 2; r++)
		{
			for (int c = -1; c < 2; c++)
			{
				builder = new UnpackedBakedQuad.Builder(Attributes.DEFAULT_BAKED_FORMAT);
				for (int i = 0; i < 4; i++)
				{
					Vector3f unit = units.get(i);
					builder.setQuadOrientation(facing);
					builder.put(0, unit.x, unit.y, unit.z, 1.0f);
					builder.put(1, d, d, d, 1);
					builder.put(2, this.sprite.getInterpolatedU(uvs[i].x), this.sprite.getInterpolatedV(uvs[i].y), 0, 1);
					builder.put(3);
				}
				quads.add(builder.build());
			}
		}
		this.faceQuads.put(facing, quads);
	}
	
	@Override
	public List<BakedQuad> getFaceQuads(EnumFacing facing)
	{
		return this.faceQuads.get(facing);
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
