package artoffood.client.screens.gui_element.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GUILabel extends GUIView {

    public FontRenderer font = Minecraft.getInstance().fontRenderer;
    public Insets insets = new Insets(0,0,0,0);

    public List<ITextProperties> lines = new ArrayList<>();
    public int textColor = Color.black.getRGB();
    public boolean useShadow = false;
    public int shadowTextColor = Color.black.getRGB();

    public boolean multiline = false;
    public Point shadowOffset = new Point(1,1);
    public int  interlineSpace = 2;

    public GUILabel(int x, int y, int width, int height) {
        super(x, y, width, height);
        setBorderWidth(0);
    }

    public void setStrings(List<String> strings) {
        lines.clear();
        lines.addAll(strings.stream().map(s -> new StringTextComponent(s)).collect(Collectors.toList()));
    }

    public void setLines(List<ITextProperties> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void setText(String text) {
        lines.clear();
        lines.add(new StringTextComponent(text));
    }

    @Override
    protected void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        List<ITextProperties> sizedLines = new ArrayList<>();
        if (multiline || lines.size() > 1) {
            int textWidth = contentFrame.width - insets.left - insets.right;
            for (ITextProperties line: lines) {
                sizedLines.addAll(font.getCharacterManager().func_238362_b_(line, textWidth, Style.EMPTY));
            }
        } else {
            if (!lines.isEmpty())
                sizedLines.add(lines.get(0));
        }

        List<IReorderingProcessor> linesProcessors = LanguageMap.getInstance().func_244260_a(sizedLines);
        for (int i = 0; i < linesProcessors.size(); i++)
            renderReorderingProcessor(matrixStack, linesProcessors.get(i), i);
    }

    protected void renderReorderingProcessor(@NotNull MatrixStack matrixStack, IReorderingProcessor proc, int lineIndex) {
        int x = contentFrame.x + insets.left;
        int y = contentFrame.y + insets.top + lineIndex * (font.FONT_HEIGHT + interlineSpace) ;
        if (useShadow)
            font.func_238422_b_(matrixStack, proc, x + shadowOffset.x, y + shadowOffset.y, shadowTextColor);
        font.func_238422_b_(matrixStack, proc, x, y, textColor);
    }
}
