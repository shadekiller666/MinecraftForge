package net.minecraftforge.client.model.breaking;

import java.util.List;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;

public class BreakingModel
{
	public static final int MAX_ROWS = 5;
	public static final int MAX_COLUMNS = 5;
	protected TextureAtlasSprite sprite;
	protected ArrayTable<Integer, Integer, BakedQuad> quads;
	
	public BreakingModel()
	{
		Range<Integer> rowRange = Range.closedOpen(0, MAX_ROWS);
		Range<Integer> columnRange = Range.closedOpen(0, MAX_COLUMNS);
		this.quads = ArrayTable.create(ContiguousSet.create(rowRange, DiscreteDomain.integers()), ContiguousSet.create(columnRange, DiscreteDomain.integers()));
		this.quads.set(0, 0, null);
	}
	
	public static class Builder
	{
		public static final int MAX_ROWS = 5;
		public static final int MAX_COLUMNS = 5;
		protected ArrayTable<Integer, Integer, BakedQuad> quads;
		protected TextureAtlasSprite particleSprite;
		protected List<BakedQuad> generalQuads;
		protected List<List<BakedQuad>> faceQuads;
		protected boolean ambient;
		protected boolean gui3d;
		protected ItemCameraTransforms transforms;
		
		public Builder(ModelBlock modelBlock)
		{
			this(modelBlock.isAmbientOcclusion(), modelBlock.isGui3d(), modelBlock.func_181682_g());
		}
		
		public Builder(IBakedModel from, TextureAtlasSprite sprite)
		{
			this(from.isAmbientOcclusion(), from.isGui3d(), from.getItemCameraTransforms());
			this.particleSprite = from.getParticleTexture();
			
			for (EnumFacing facing : EnumFacing.values())
			{
				
			}
		}
		
		public Builder(boolean ambient, boolean gui3d, ItemCameraTransforms transforms)
		{
			this.generalQuads = Lists.<BakedQuad>newArrayList();
			this.faceQuads = Lists.<List<BakedQuad>>newArrayListWithCapacity(6);
			
			for (EnumFacing facing : EnumFacing.values())
			{
				this.faceQuads.add(Lists.<BakedQuad>newArrayList());
			}
			
			this.ambient = ambient;
			this.gui3d = gui3d;
			this.transforms = transforms;
		}
		
		private void addFaceQuads(IBakedModel from, TextureAtlasSprite sprite, EnumFacing facing)
		{
			for (BakedQuad baked : from.getGeneralQuads())
			{
				this.addFaceQuad(facing, DividableQuad.create(baked, sprite));
			}
		}
		
		public BreakingModel.Builder addFaceQuad(EnumFacing facing, BakedQuad[] quads)
		{
			((List<BakedQuad>) this.faceQuads.get(facing.ordinal())).addAll(Lists.newArrayList(quads));
			return this;
		}
		
		
	}
}
