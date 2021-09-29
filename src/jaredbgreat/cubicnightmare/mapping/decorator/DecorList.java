package jaredbgreat.cubicnightmare.mapping.decorator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jared Blackburn
 */
public class DecorList implements IDecorSelect {
    private final List<IDecorSelect> list;
    private final Random random;
    
    
    public DecorList() {
        list = new ArrayList<>();
        random = new Random();
    }
    
    
    public DecorList(Random random) {
        list = new ArrayList<>();
        this.random = random;
    }

    @Override
    public Decoration getDecoration() {
        return list.get(random.nextInt(list.size())).getDecoration();
    }
    
    
    public void add(IDecorSelect subselector) {
        list.add(subselector);
    }
    
    
    public void add(Decoration decoration) {
        list.add(new DecorLeaf(decoration));
    }
    
    
    public void addAll(Collection<IDecorSelect> collection) {
        list.addAll(collection);
    }
    
    
    public void addAll(IDecorSelect... decorations) {
        for(int i = 0; i < decorations.length; i++) {
            list.add(decorations[i]);
        }
    }
    
    
    public void addAll(Decoration... decorations) {
        for(int i = 0; i < decorations.length; i++) {
            list.add(new DecorLeaf(decorations[i]));
        }
    }
    
}
