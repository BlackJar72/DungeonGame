package jaredbgreat.cubicnightmare.mapping.decorator;

/**
 *
 * @author Jared Blackburn
 */
public class DecorLeaf implements IDecorSelect {
    private final Decoration decoration;
    
    public DecorLeaf(Decoration decoration) {
        this.decoration = decoration;
    }
    

    @Override
    public Decoration getDecoration() {
        return decoration;
    }
    
}
