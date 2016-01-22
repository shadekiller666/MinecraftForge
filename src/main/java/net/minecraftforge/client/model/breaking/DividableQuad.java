package net.minecraftforge.client.model.breaking;

import java.util.Arrays;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class DividableQuad
{
	public static BakedQuad[] create(BakedQuad from, TextureAtlasSprite sprite)
	{
		int[] vertexData = Arrays.copyOf(from.getVertexData(), from.getVertexData().length);
		int tintIndex = from.getTintIndex();
		EnumFacing facing = FaceBakery.getFacingFromVertexData(from.getVertexData());
		
		return null;
	}
	
	private void remapQuad(int[] vertexData, EnumFacing facing)
	{
		for (int i = 0; i < 4; i++)
		{
			this.remapVert(vertexData, i, facing);
		}
	}
	
	private void remapVert(int[] vertexData, int vertex, EnumFacing facing)
	{
		int i = 7 * vertex;
		float x = Float.intBitsToFloat(vertexData[i]);
		float y = Float.intBitsToFloat(vertexData[i]);
		float z = Float.intBitsToFloat(vertexData[i]);
		float u = 0.0f;
		float v = 0.0f;
		
		switch (facing)
		{
		case DOWN:
			u = x;
			v = 1.0f - z;
			break;
		case UP:
			u = x;
			v = z;
			break;
		case NORTH:
			u = 1.0f - x;
			v = 1.0f - y;
			break;
		case SOUTH:
			u = x;
			v = 1.0f - y;
			break;
		case WEST:
			u = z;
			v = 1.0f - y;
			break;
		case EAST:
			u = 1.0f - z;
			v = 1.0f - y;
			break;
		}
		
		if (u < 0.0f) ;
		if (u > 1.0f) ;
		if (v < 0.0f) ;
		if (v > 1.0f) ;
	}
}
