package jaredbgreat.cubicnightmare.entities.mobstates;

import jaredbgreat.cubicnightmare.entities.IMob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This represents a list of entities (typically mobile entities, or "mobs") 
 * for AI or other relevant processing.  Techincally this wraps an ArrayList 
 * and uses the process method to automate proper use of the list, including 
 * automated removal of entities which no longer belong in the list, having 
 * lost the state it repressents.  This is intended as a more efficient way 
 * to tag an intity with a state or behavior, allowing related code to only 
 * be run on entities to which it pertains, rather than simply checking every 
 * entity for states that might be rare or often not pressent.
 * 
 * Techinically, this wraps an ArrayList, and uses the process() methode to run 
 * code defined during construction through a classes extending IMobState. If 
 * the mob needs to be removed from the list due to the results of the code 
 * this is run it will automatically and safely do so after all mobs in the 
 * list have run their code.
 * 
 * I MAY END UP NOT USING THIS BECAUSE JME3 HAS ITS OWN SYSTEM FOR HANDLING 
 * ENTITIES AND THIS CLASHES WITH IT!  It would be nice of engines supported 
 * this kind of data oriented approach, but most don't and some devs (and 
 * many designers) would not want to fool with such a system baked into the 
 * engine.
 * 
 * @author Jared Blackburn
 */
public final class MobStateList implements List<IMob> {
    private final List<IMob> mobs;
    private final List<IMob> toRemove;
    private final IMobState processor;
    
    
    public MobStateList(IMobState code) {
        processor  = code;
        mobs       = new ArrayList<>();
        toRemove   = new ArrayList<>();
    }
    
    
    /**
     * This method will perfomrm the called-for operations on each mob in the list, 
     * then remove any that should no longer be include (effectively clearing the 
     * represented state).
     */
    public void process() {
        int n = mobs.size();
        for(int i = 0; i < n; i++) {
            IMob mob = mobs.get(i);
            if(processor.process(mob)) toRemove.add(mob);
        }
        if(!toRemove.isEmpty()) {
            mobs.removeAll(toRemove);
            toRemove.clear();
        }
    }
    
    
    @Override
    public boolean add(IMob mob) {
        return mobs.add(mob);
    }
    
    
    public boolean remove(IMob mob) {
        return mobs.remove(mob);
    }
    
    
    @Override
    public boolean addAll(Collection<? extends IMob> col) {
        return mobs.addAll(col);
    }
    
    
    public boolean addAll(MobStateList list) {
        return mobs.addAll(list.mobs);
    }

    
    @Override
    public boolean removeAll(Collection<?> c) {
        return mobs.removeAll(c);
    }
    
    
    public boolean removeAll(MobStateList list) {
        return mobs.removeAll(list.mobs);
    }
    
    
    public boolean equivalent(MobStateList other) {
        return (this.processor.getClass() == other.processor.getClass());
    }
    
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof MobStateList) {
            MobStateList other = (MobStateList)o;
            return ((this.processor.getClass() == other.processor.getClass()) && 
                    (mobs == other.mobs));
        }
        return false;
    }
    
    
    @Override
    public int hashCode() {
        return (processor.getClass().hashCode() ^ mobs.hashCode());
    }
    
    
    @Override
    public boolean isEmpty() {
        return mobs.isEmpty();
    }
    
    
    @Override
    public void clear() {
        mobs.clear();
    }

    @Override
    public int size() {
        return mobs.size();
    }

    @Override
    public boolean contains(Object o) {
        return mobs.contains(o);
    }

    @Override
    public Iterator<IMob> iterator() {
        return mobs.iterator();
    }

    @Override
    public Object[] toArray() {
        return mobs.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return mobs.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        if(o instanceof IMob) return mobs.remove(o);
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mobs.containsAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends IMob> c) {
        return mobs.addAll(index, c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return mobs.retainAll(c);
    }

    @Override
    public IMob get(int index) {
        return mobs.get(index);
    }

    @Override
    public IMob set(int index, IMob element) {
        return mobs.set(index, element);
    }

    @Override
    public void add(int index, IMob element) {
        mobs.add(index, element);
    }

    @Override
    public IMob remove(int index) {
        return mobs.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return mobs.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return mobs.lastIndexOf(o);
    }

    @Override
    public ListIterator<IMob> listIterator() {
        return mobs.listIterator();
    }

    @Override
    public ListIterator<IMob> listIterator(int index) {
        return mobs.listIterator(index);
    }

    @Override
    public List<IMob> subList(int fromIndex, int toIndex) {
        MobStateList msl = new MobStateList(processor);
        msl.addAll(mobs.subList(fromIndex, toIndex));
        return msl;
    }
    
    
}
