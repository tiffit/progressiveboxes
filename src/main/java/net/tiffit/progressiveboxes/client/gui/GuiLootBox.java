package net.tiffit.progressiveboxes.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.tiffit.progressiveboxes.ProgressiveBoxes;

public class GuiLootBox extends GuiScreen {

	private static ResourceLocation BOX = new ResourceLocation(ProgressiveBoxes.MODID, "textures/items/box.png");
	
	private float r, g, b;
	private int color;
	private List<ItemStack> stacks = new ArrayList<>();
	private int guiLeft, guiTop;
	private int animDur = 0;
	
	public GuiLootBox(int color, List<ItemStack> stacks){
        r = (float)(color >> 16 & 255) / 255.0F;
        g = (float)(color >> 8 & 255) / 255.0F;
        b = (float)(color & 255) / 255.0F;
        this.color = color;
        for(ItemStack stack : stacks){
        	if(!stack.isEmpty()){
        		this.stacks.add(stack);
        	}
        }
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(animDur < 100)animDur+=2;
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(BOX);
        GlStateManager.enableBlend();
        GlStateManager.color(r, g, b, (300-animDur)/300F);
        drawScaledCustomSizeModalRect(guiLeft, guiTop, 0, 0, 1, 1, 100, 100, 1, 1);
        GlStateManager.color(1, 1, 1, 1);
        drawCenteredString(fontRenderer, "Loot Box Content", this.width/2, this.height/2 - 60, color);
        ItemStack highlight = null;
        RenderHelper.enableGUIStandardItemLighting();
        
        float offsetX = animDur/100F;
        float offsetY = animDur/100F;
        for(int i = 0; i < stacks.size(); i++){
        	ItemStack stack = stacks.get(i);
        	int ix = (int) (this.width/2 - 8 + (-(stacks.size()-1)*10 + i*20)*offsetX);
        	int iy = (int) (this.height/2 - 8 - 40*offsetY);
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stack, ix, iy);
            String text = "";
            if(stack.getCount() > 1)text = stack.getCount() + "";
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, stack, ix, iy, text);
            if(mouseX > ix && mouseX < ix+16 && mouseY > iy && mouseY < iy+16){
            	 highlight = stack;
            }
        }
        if(highlight != null)renderToolTip(highlight, mouseX, mouseY);
	}
	
    public void initGui()
    {
        super.initGui();
        this.guiLeft = (this.width - 100) / 2;
        this.guiTop = (this.height - 100) / 2;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
    	return false;
    }
	
}
