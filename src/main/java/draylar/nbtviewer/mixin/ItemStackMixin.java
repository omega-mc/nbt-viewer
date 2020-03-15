package draylar.nbtviewer.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean hasTag();

    @Shadow public abstract CompoundTag getTag();

    @Inject(at = @At("RETURN"), method = "getTooltip", locals = LocalCapture.CAPTURE_FAILHARD)
    private void appendNBTTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        if (context.isAdvanced()) {
            if (this.hasTag()) {
                this.getTag().getKeys().forEach(key -> {
                    list.add(new LiteralText(key + ": " + this.getTag().get(key).toString()));
                });
            }
        }
    }
}
