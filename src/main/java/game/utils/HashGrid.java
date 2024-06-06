package game.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class HashGrid<T extends HashGrid.BoundedObject> {
    private final HashMap<Integer, ArrayList<T>> _cells = new HashMap<>();
    // Used to convert X coords into column index
    private int _cellWidthShift = 0;
    // Used to convert Y coords into row index
    private int _cellHeightShift = 0;
    
    public HashGrid() {
        this.setCellWidth(32);
        this.setCellHeight(32);
    }
    
    public interface BoundedObject {
        Vector getPosition();
        
        float getWidth();
        
        float getHeight();
    }
    
    public void clear() {
        this._cells.clear();
    }
    
    public void insert(T item) {
        int startX = this._getColumnIndex(item.getPosition().getX());
        int startY = this._getRowIndex(item.getPosition().getY());
        int endX = this._getColumnIndex(item.getPosition().getX() + item.getWidth()) + 1;
        int endY = this._getRowIndex(item.getPosition().getY() + item.getHeight()) + 1;
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int key = this._getHashKey(x, y);
                if (!this._cells.containsKey(key)) {
                    this._cells.put(key, new ArrayList<>());
                }
                this._cells.get(key).add(item);
            }
        }
    }
    
    public ArrayList<T> retrieve(BoundedObject bounds) {
        ArrayList<T> result = new ArrayList<>();
        int startX = this._getColumnIndex(bounds.getPosition().getX());
        int startY = this._getRowIndex(bounds.getPosition().getY());
        int endX = this._getColumnIndex(bounds.getPosition().getX() + bounds.getWidth()) + 1;
        int endY = this._getRowIndex(bounds.getPosition().getY() + bounds.getHeight()) + 1;
        
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                int key = this._getHashKey(x, y);
                ArrayList<T> items = this._cells.get(key);
                if (items != null) {
                    result.addAll(items);
                }
            }
        }
        
        return result;
    }
    
    public void setCellWidth(int cellWidth) {
        this._cellWidthShift = (int) Math.floor(log2(cellWidth));
    }
    
    public void setCellHeight(int cellHeight) {
        this._cellHeightShift = (int) Math.floor(log2(cellHeight));
    }
    
    private static double log2(int N) {
        return (Math.log(N) / Math.log(2));
    }
    
    /**
     * Get the hash key of X and Y coordinates. This assumes that
     * X and Y have been converted into column and row indices respectively.
     *
     * @param x The column index.
     * @param y The row index.
     * @returns {number} A number that represents the hash key.
     */
    private int _getHashKey(int x, int y) {
        // Total of 32 bits
        // Let X take the left 16 bits and Y take the right 16 bits
        return (x << 16) | y;
    }
    
    /**
     * Convert Y coordinate into row index.
     *
     * @param y The Y coordinate to convert into row index.
     * @returns {number} The row index of the Y coordinate.
     */
    private int _getRowIndex(float y) {
        return (int) y >> this._cellHeightShift;
    }
    
    /**
     * Convert X coordinate into column index.
     *
     * @param x The X coordinate to convert into column index.
     * @returns {number} The column index of the X coordinate.
     */
    private int _getColumnIndex(float x) {
        return (int) x >> this._cellWidthShift;
    }
}