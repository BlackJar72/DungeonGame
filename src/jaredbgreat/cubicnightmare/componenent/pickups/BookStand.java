package jaredbgreat.cubicnightmare.componenent.pickups;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jaredbgreat.cubicnightmare.entities.Player;
import java.util.List;

/**
 *
 * @author Jared Blackburn
 */
public class BookStand implements IPickupEffect {
    private final String name;
    private final Node node;
    private Spatial book;
    
    
    public BookStand(String name, Spatial node) {
        this.name = name;
        if(node instanceof Node) {
            this.node = (Node)node;
        } else {
            this.node = new Node();
            this.node.attachChild(node);
        }
        this.node.setName(name);
        book = this.node.getChild("Book");
    }
    

    @Override
    public void effect(Player player) {
        if(book != null) {
            player.addScore(25);
            List<Spatial> children =  node.getChildren();
            for(Spatial child : children) {
                System.out.println(child.getName());
            }
            node.detachChild(book);
            book = null;
        }
    }
    
}
