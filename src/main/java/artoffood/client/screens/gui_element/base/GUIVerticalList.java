package artoffood.client.screens.gui_element.base;


import artoffood.ArtOfFood;
import artoffood.client.screens.Textures;
import artoffood.client.utils.Texture;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GUIVerticalList<T, C extends GUIListCell<T>> extends GUIView implements GUIScroller.Delegate, GUIListCell.Delegate<T> {

    public interface Delegate<T> {
        void didSelectCell(GUIListCell<T> cell);
    }

    private static final Logger logger = LogManager.getLogger(ArtOfFood.MOD_ID);
    private static final int DEFAULT_BACK_COLOR = Color.decode("#8B8B8B").getRGB();

    protected Class<C> cellsClass;
    protected List<C> cells;
    protected GUIScroller scroller;
    protected GUIScrollableView scrollableView;

    protected Texture scrollTexture = Textures.MIDDLE_SCROLLER;
    protected int intercellSpace = 0;
    protected int scrollSeparatorWidth = 1;
    protected float mouseScrollingSpeedCoeeficient = 20;

    public int separatorColor = Color.decode("#373737").getRGB();
    public @Nullable Delegate<T> delegate;

    public GUIVerticalList(Class<C> cellsClass, List<T> models, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.cellsClass = cellsClass;
        this.backColor = DEFAULT_BACK_COLOR;

        final int cellsWidth = contentFrame.width - scrollSeparatorWidth - scrollTexture.uWidth;
        scrollableView = new GUIScrollableView(0, 0, cellsWidth, contentFrame.height);
        scrollableView.setBorderWidth(0);
        addChild(scrollableView);

        cells = new ArrayList<>(models.size());
        int currentY = 0;
        for (T model: models) {
            try {
                final C newCell = cellsClass.newInstance();
                newCell.setModel(model);
                newCell.setFrame(0, currentY, cellsWidth);
                newCell.delegate = this;
                currentY += newCell.getFrame().height + intercellSpace;
                cells.add(newCell);
                scrollableView.addChild(newCell);
            } catch (Exception e) {
                logger.error("Invalid cell class in GUIVerticalList. Probably " + cellsClass.getSimpleName() + " has no public constructor without arguments");
            }
        }

        setupScroll();
    }

    protected void setupScroll() {
        final int scrollerX = contentFrame.width - scrollTexture.uWidth;
        final int maxDisplacement = contentFrame.height - scrollTexture.vHeight;
        scroller = new GUIScroller(scrollTexture, scrollerX, 0, scrollTexture.uWidth, scrollTexture.vHeight, maxDisplacement);
        scroller.delegate = this;
        addChild(scroller);
    }

    @Override
    public void preChildsRender(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.preChildsRender(matrixStack, mouseX, mouseY, partialTicks);

        final int maxX = contentFrame.x + contentFrame.width;
        final int maxY = contentFrame.y + contentFrame.height;
        final int separatorX = maxX - scrollTexture.uWidth;
        fill(matrixStack, separatorX, contentFrame.y, separatorX - scrollSeparatorWidth, maxY, separatorColor);
    }

    @Override
    public void didScroll(GUIScroller scroll, double position) {
        scrollableView.setRelativeOffset(0, position);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (super.mouseScrolled(mouseX, mouseY, delta)) return true;

        if (absoluteFrame.contains(mouseX, mouseY)) {
            final int relativeStep = Math.round((float)scroller.getMaxDisplacement() / scrollableView.contentHeight);
            final int step = Math.round(relativeStep * mouseScrollingSpeedCoeeficient * (float)delta);
            scroller.scrollTo(scroller.getCurrentDisplacement() + step);
            return true;
        }

        return false;
    }

    @Override
    public void didClickCell(GUIListCell<T> cell) {
        if (cell.isSelected) return;

        cells.forEach( c -> c.setIsSelected(false));
        cell.setIsSelected(true);
        if (delegate != null && cellsClass.isAssignableFrom(cell.getClass()))
            delegate.didSelectCell((C) cell);
    }
}
