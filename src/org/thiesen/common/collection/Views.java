package org.thiesen.common.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList.Builder;


public class Views {

    public static <K,V> Map<K,V> of( final Map<K,V>... maps ) {
        return asOne( Arrays.asList( maps ) );
    }
    
    
    public static <K,V> Map<K,V> of( final Map<K,V> map1, final Map<K,V> map2 ) {
        return asOne( ImmutableList.of( map1, map2 ) );
    }
    
    private static <K,V> Map<K,V> asOne( final List<Map<K,V>> mapList ) {
        
        return new Map<K, V>() {

            @Override
            public void clear() {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public boolean containsKey( final Object key ) {
                return anyWithPredicate( new Predicate<Map<K,V>>() {

                    @Override
                    public boolean apply( Map<K,V> map ) {
                        return map.containsKey( key );
                    }
                });
            }

            private boolean anyWithPredicate( final Predicate<Map<K,V>> predicate ) {
                return Iterables.any( mapList, predicate );
            }

            private boolean allWithPredicate( final Predicate<Map<K,V>> predicate ) {
                return Iterables.any( mapList, predicate );
            }


            @Override
            public boolean containsValue( final Object value ) {
                return anyWithPredicate( new Predicate<Map<K,V>>() {

                    @Override
                    public boolean apply( Map<K,V> map ) {
                        return map.containsValue( value );
                    }
                });

            }

            @Override
            public Set<java.util.Map.Entry<K, V>> entrySet() {
                return of( Lists.transform( mapList, new Function<Map<K,V>, Set<java.util.Map.Entry<K, V>>>() {

                    @Override
                    public Set<java.util.Map.Entry<K, V>> apply( Map<K, V> map ) {
                        return map.entrySet();
                    }
                } 
                ) );
            }

            @Override
            public V get( Object key ) {
                for ( final Map<K,V> map : mapList ) {
                    if ( map.containsKey( key ) ) {
                        return map.get( key );
                    }
                }

                return null;
            }

            @Override
            public boolean isEmpty() {
                return allWithPredicate( new Predicate<Map<K,V>>() {
                    public boolean apply( Map<K, V> map ) {
                        return map.isEmpty();
                    }
                } );
            }

            @Override
            public Set<K> keySet() {
                return of( Lists.transform( mapList, new Function<Map<K,V>, Set<K>>() {

                    public Set<K> apply( Map<K, V> map ) {
                        return map.keySet();
                    }
                } 
                ) );
            }

            @Override
            public V put( K key, V value ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public void putAll( Map<? extends K, ? extends V> m ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public V remove( Object key ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public int size() {
                int size = 0;
                for ( final Map<K,V> map : mapList ) {
                    size += map.size();
                }

                return size;
            }

            @Override
            public Collection<V> values() {

                return asOne( Lists.transform( mapList, new Function<Map<K,V>, Collection<V>>() {

                    public Collection<V> apply( Map<K, V> map ) {
                        return map.values();
                    }
                } ) );
            }
        };

    }
    
    public static <V> Collection<V> of( Collection<V>... collections ) {
        return asOne(Arrays.asList( collections ));
    }
    
    public static <V> Collection<V> asOne( final List<Collection<V>> collections ) {
        return new Collection<V>() {

            @Override
            public boolean add( V e ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public boolean addAll( Collection<? extends V> c ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public void clear() {
                throw new UnsupportedOperationException("Views are read only");
            }

            private boolean anyWithPredicate( final Predicate<Collection<V>> predicate ) {
                return Iterables.any( collections, predicate );
            }

            private boolean allWithPredicate( final Predicate<Collection<V>> predicate ) {
                return Iterables.any( collections, predicate );
            }
            
            @Override
            public boolean contains( final Object o ) {
                return anyWithPredicate( new Predicate<Collection<V>>() {

                    @Override
                    public boolean apply( Collection<V> collection ) {
                        return collection.contains( o );
                    }
                    
                } );
                
            }

            @Override
            public boolean containsAll( Collection<?> c ) {
                return makeAllValueBuilder( collections ).build().containsAll( c );
            }

            @Override
            public boolean isEmpty() {
                return allWithPredicate( new Predicate<Collection<V>>() {

                    @Override
                    public boolean apply( Collection<V> collection ) {
                        return collection.isEmpty();
                    }
                } );
                
            }

            @Override
            public Iterator<V> iterator() {
                return Iterables.concat( collections ).iterator();
            }

            @Override
            public boolean remove( Object o ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public boolean removeAll( Collection<?> c ) {
                throw new UnsupportedOperationException("Views are read only");                
            }

            @Override
            public boolean retainAll( Collection<?> c ) {
                throw new UnsupportedOperationException("Views are read only");
            }

            @Override
            public int size() {
                int size = 0;
                for ( final Collection<V> collection : collections ) {
                    size += collection.size();
                }
                return size;
            }

            @Override
            public Object[] toArray() {
                Builder<Object> builder = makeAllValueBuilder( collections );
                return builder.build().toArray();
            }

            private Builder<Object> makeAllValueBuilder( final List<Collection<V>> collections ) {
                Builder<Object> builder = ImmutableList.builder();
                for ( final Collection<V> collection : collections ) {
                    builder.addAll( collection );
                }
                return builder;
            }

            public <T> T[] toArray(T[] a) {
                Builder<Object> builder = makeAllValueBuilder( collections );
                return builder.build().toArray(a);
            }
            
        };
    }
    
    
    public static <V> Set<V> of( Set<V> seta, Set<V> setb ) {
        return Sets.union( seta, setb );
    }
    
    public static <V> Set<V> of( Set<V>... sets ) {
        Set<V> retval = Collections.emptySet();

        for ( final Set<V> set : sets ) {
            retval = Sets.union( retval, set ); 
        }

        return retval;
    }

    public static <V> Set<V> of( Iterable<Set<V>> sets ) {
        Set<V> retval = Collections.emptySet();

        for ( final Set<V> set : sets ) {
            retval = Sets.union( retval, set ); 
        }

        return retval;
    }



}
