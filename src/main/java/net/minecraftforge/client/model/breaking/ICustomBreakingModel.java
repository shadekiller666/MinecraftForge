package net.minecraftforge.client.model.breaking;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;

public interface ICustomBreakingModel
{
	default IBakedModel getBreakingModel(IBakedModel targetLayer, TextureAtlasSprite sprite)
	{
		return (new SimpleBakedModel.Builder(targetLayer, sprite)).makeBakedModel();
	}
}
