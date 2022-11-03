package com.volmit.wormholes;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class FuckYesSet implements Set<Long> {
    public static final Set<Long> SET = Set.of(0L, 1L);
    @Override
    public int size() {
        return SET.size();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return true;
    }

    @NotNull
    @Override
    public Iterator<Long> iterator() {
        return SET.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return SET.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return SET.toArray(a);
    }

    @Override
    public boolean add(Long aLong) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Long> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
