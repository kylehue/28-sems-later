package skins;

import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class ProgressBarImageSkin extends SkinBase<ProgressBar> {
    private final StackPane container;
    private final ImageView baseImage;
    private final ImageView barImage;
    private final double barOffsetX;
    private final double barOffsetY;
    private final Rectangle barClip = new Rectangle();
    
    public ProgressBarImageSkin(
        ProgressBar progressBar,
        Image baseImage,
        Image barImage,
        double barOffsetX,
        double barOffsetY
    ) {
        super(progressBar);
        this.barOffsetX = barOffsetX;
        this.barOffsetY = barOffsetY;
        
        this.container = new StackPane();
        this.container.prefWidthProperty().bind(progressBar.prefWidthProperty());
        this.container.minWidthProperty().bind(progressBar.minWidthProperty());
        this.container.maxWidthProperty().bind(progressBar.maxWidthProperty());
        this.container.prefHeightProperty().bind(progressBar.prefHeightProperty());
        this.container.minHeightProperty().bind(progressBar.minHeightProperty());
        this.container.maxHeightProperty().bind(progressBar.maxHeightProperty());
        
        this.baseImage = new ImageView(baseImage);
        this.baseImage.setPreserveRatio(true);
        this.baseImage.fitWidthProperty().bind(progressBar.widthProperty());
        this.baseImage.fitHeightProperty().bind(progressBar.heightProperty());
        
        this.barImage = new ImageView(barImage);
        this.barImage.setPreserveRatio(false);
        
        this.barImage.setClip(barClip);
        
        Group group = new Group(this.baseImage, this.barImage);
        this.container.getChildren().add(group);
        
        getChildren().setAll(this.container);
        this.initListeners();
    }
    
    private void initListeners() {
        ProgressBar progressBar = getSkinnable();
        
        progressBar.widthProperty().addListener(e -> {
            fixBarPosition();
        });
        
        progressBar.heightProperty().addListener(e -> {
            fixBarPosition();
        });
        
        progressBar.boundsInParentProperty().addListener(e -> {
            fixBarPosition();
        });
        
        progressBar.progressProperty().addListener((o, o1, percentage) -> {
            updateProgress(percentage.doubleValue());
        });
    }
    
    private void fixBarPosition() {
        double progress = getSkinnable().getProgress();
        this.barImage.setTranslateX(barOffsetX * getWidthRadio());
        this.barImage.setTranslateY(barOffsetY * getHeightRadio());
        barClip.setWidth(
            this.barImage.getImage().getWidth() * progress * getWidthRadio()
        );
        barClip.setHeight(this.barImage.getImage().getHeight() * getHeightRadio());
    }
    
    private void updateProgress(double progress) {
        barClip.setWidth(
            (int) (this.barImage.getImage().getWidth() * progress * getWidthRadio())
        );
    }
    
    private double getWidthRadio() {
        return getSkinnable().getWidth() / baseImage.getImage().getWidth();
    }
    
    private double getHeightRadio() {
        return getSkinnable().getHeight() / baseImage.getImage().getHeight();
    }
    
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(100, leftInset + baseImage.prefWidth(getSkinnable().getWidth()) + rightInset);
    }
    
    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Math.max(5, baseImage.prefHeight(width)) + topInset + bottomInset;
    }
    
    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefHeight(width);
    }
    
    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return getSkinnable().prefWidth(height);
    }
}