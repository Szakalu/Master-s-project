package pl.lodz.uni.math.graph.viewer;

import java.awt.event.MouseEvent;
import java.util.Random;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.graphicGraph.GraphicGraph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.util.MouseManager;

public class GraphMouseManager implements MouseManager {

	 /**
     * The view this manager operates upon. 
     */ 
    protected View view; 


    /**
     * The graph to modify according to the view actions. 
     */ 
    protected GraphicGraph graph;


    protected GraphicElement element; 

    
    public void init(GraphicGraph gg, View view) {
        this.graph = gg;
        this.view = view;
        view.addMouseListener(this);
        view.addMouseMotionListener(this);
    }

    
    public void release() {
        view.removeMouseListener(this);
        view.removeMouseMotionListener(this);
    }

    
    public void mouseClicked(MouseEvent me) {
        element = view.findNodeOrSpriteAt(me.getX(), me.getY());
        if(element != null){
            Random r = new Random();
            element.setAttribute("ui.style", "fill-color: rgb("+r.nextInt(256)+","+r.nextInt(256)+","+r.nextInt(256)+");");
        }

    }

    
    public void mousePressed(MouseEvent me) {
    }

    
    public void mouseReleased(MouseEvent me) {
    }

    
    public void mouseEntered(MouseEvent me) {
    }

    
    public void mouseExited(MouseEvent me) {
    }

    
    public void mouseDragged(MouseEvent me) {
    }

    
    public void mouseMoved(MouseEvent me) {
    
    }
	
}
