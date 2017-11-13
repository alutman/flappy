package flappy.entity;

import flappy.FlappyBird;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;

/**
 * Draws a random cityscape background
 */
public class Background implements GameEntity {

    private ArrayList<Building> buildings = new ArrayList<Building>();

    private final int maxFloors = 9;
    private final int minFloors = 2;
    private final int maxWindows = 6;
    private final int minWindows = 2;
    private final int minWidth = 10 + (minWindows * 40);

    private final int SCROLL_VELOCITY = -1;

    public Background() {
        reset();
    }

    @Override
    public void reset() {
        buildings.clear();

        addBuilding(FlappyBird.WIDTH);
        while(first().x > 0) {
            tick();
        }
    }

    private Building first() {
        Building first = null;
        for(Building b : buildings) {
            if(first == null) first = b;
            if(first.x > b.x) {
                first = b;
            }
        }
        return first;
    }

    private Building last() {
        Building last = null;
        for(Building b : buildings) {
            if(last == null) last = b;
            if(last.x < b.x) {
                last = b;
            }
        }
        return last;
    }

    private final Color[] colors = {Color.GRAY, Color.WHITE, Color.ORANGE, Color.PINK};

    private Random rand = new Random();
    private Color randomColor() {
        Color c = colors[rand.nextInt(colors.length)];
        while(last() != null && c.equals(last().color)) { // Don't repeat colors
            c = colors[rand.nextInt(colors.length)];
        }
        return c;
    }

    private int randomFloors() {
        int floors = (Math.abs(rand.nextInt()) % (maxFloors - minFloors)) + minFloors;
        while(last() != null && floors == last().floors) { // Don't repeat heights
            floors = (Math.abs(rand.nextInt()) % (maxFloors - minFloors)) + minFloors;
        }
        return floors;
    }
    private int randomWindows() {
        return (Math.abs(rand.nextInt()) % (maxWindows - minWindows)) + minWindows;
    }
    private int randomLoc(int minimumX) {
        return rand.nextInt(minWidth) + minimumX;
    }
    private int randomDepth() {
        int r = rand.nextInt(5)+1;
        if(rand.nextBoolean()) { // Give a greater chance for closer buildings
            r -= 2;
        }
        return Math.max(r, 1);
    }

    private void addBuilding(int minimumX) {
        Building newBuilding = new Building(randomLoc(minimumX),randomWindows(),randomFloors(), randomDepth(), randomColor());
        buildings.add(newBuilding);
    }
    private void addBuilding(int minimumX, int maximumDepth) {
        Building newBuilding = new Building(randomLoc(minimumX),randomWindows(),randomFloors(), Math.min(randomDepth(), maximumDepth), randomColor());
        buildings.add(newBuilding);
    }

    // Positive if mostly deep buildings, negative if mostly shallow
    private int depthSum() {
        int sum = 0;
        for(Building b : buildings) {
            sum += (b.depth - 3);
        }
        return sum;
    }



    @Override
    public void tick() {
        for(Building b : buildings) {
            // Reduce the velocity depending on how deep a building is (parallax)
            float speed = (1f/(float)b.depth) * (float) SCROLL_VELOCITY; //fkn java, i'm doing float arithmetic, not int
            b.update(speed);
        }

        if(last().x < FlappyBird.WIDTH - minWidth) {
            addBuilding(FlappyBird.WIDTH);
        }
        if(depthSum() > 0) {
            addBuilding(FlappyBird.WIDTH, 2); // Too many deep buildings, add some shallow to balance it out
        }

        ArrayList<Building> toRemove = new ArrayList<Building>();
        for(Building b : buildings) {
            if(b.width + b.x <= 0) {
                toRemove.add(b);
            }
        }
        buildings.removeAll(toRemove);
    }

    @Override
    public void paint(Graphics2D g) {
        // Sort buildings into their depths
        Map<Integer, ArrayList<Building>> depths = new HashMap<Integer, ArrayList<Building>>();
        for (Building b : buildings) {
            if(depths.get(b.depth) == null) {
                depths.put(b.depth, new ArrayList<Building>());
            }
            depths.get(b.depth).add(b);
        }
        ArrayList<Integer> integers = new ArrayList<Integer>(depths.keySet());
        Collections.sort(integers);
        Collections.reverse(integers);

        // Draw deepest buildings first
        for(Integer i : integers) {
            for(Building b : depths.get(i)) {
                b.draw(g);
            }
        }
    }
}

class Building {


    // Definitions of how to draw buildings
    private int window_width = 30;
    private int window_height = 30;
    private int border_padding = 30;
    private int window_padding = 10;
    private int floor_padding = 20;


    // Convert windows/floors into an actual distance
    public int realWidth(int windows) {
        return (windows * window_width) +
                (border_padding *2 ) +
                (window_padding * (windows-1));
    }
    public int realHeight(int floors) {
        return (floors * window_height) +
                (floors * floor_padding) +
                (border_padding);
    }

    final int floors;
    final int windows;
    final Color color;
    final int depth;

    float x;
    float y;
    int width;
    int height;


    public Building(int x, int windows, int floors, int depth, Color color) {
        // Reduce the size for deeper buildings
        int depth_adj = (depth-1) * 2;
        window_width -= depth_adj;
        window_height -= depth_adj;
        border_padding -= depth_adj;
        window_padding -= depth_adj;
        floor_padding -= depth_adj;

        this.width = realWidth(windows);
        this.height = realHeight(floors);
        this.floors = floors;
        this.windows = windows;
        this.color = color;
        this.depth = depth;
        this.x = x;
        this.y = FlappyBird.HEIGHT - this.height;

    }

    public void update(float x) {
        this.x += x;
    }

    private void drawBorder(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.drawRect(x,y, width, height);
        g2d.drawRect(x+1,y+1, width-2, height-2);
    }

    // Draw a rectangle containing rows of inner rectangles (like a building)
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform transform = g2d.getTransform();
        g2d.translate(this.x, this.y);
        g2d.setColor(color);
        g2d.fillRect(0, 0, this.width, this.height);

        g2d.setColor(Color.black);
        drawBorder(g2d, 0,0, this.width, this.height);

        int height = 0;
        int width = 0;
        height += border_padding;
        while(height + window_height < this.height) {
            width += border_padding;
            while(width + window_width < this.width) {
                g2d.setColor(Color.cyan);
                g2d.fillRect(width, height, window_width, window_height);
                g2d.setColor(Color.black);
                drawBorder(g2d, width, height, window_width, window_height);
                width += window_width;
                width += window_padding;
            }
            width = 0;
            height += window_height;
            height += floor_padding;
        }

        // Apply haze to deep buildings
        g2d.setColor(new Color(255, 255, 255, 25 * depth));
        g2d.fillRect(0, 0, this.width, this.height);

        g2d.setTransform(transform);

    }

}
