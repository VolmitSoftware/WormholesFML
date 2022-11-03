package com.volmit.wormholes;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class LongProperty extends Property<Long> {
   private final ImmutableSet<Long> values;

   protected LongProperty(String pName, long pMin, long pMax) {
      super(pName, Long.class);
      if (pMax <= pMin) {
         throw new IllegalArgumentException("Max value of " + pName + " must be greater than min (" + pMin + ")");
      } else {
         Set<Long> set = Sets.newHashSet();

         for(long i = pMin; i <= pMax; ++i) {
            set.add(i);
         }

         this.values = ImmutableSet.copyOf(set);
      }
   }

   public Collection<Long> getPossibleValues() {
      return this.values;
   }

   public boolean equals(Object pOther) {
      if (this == pOther) {
         return true;
      } else if (pOther instanceof LongProperty && super.equals(pOther)) {
         LongProperty integerproperty = (LongProperty)pOther;
         return this.values.equals(integerproperty.values);
      } else {
         return false;
      }
   }

   public int generateHashCode() {
      return 31 * super.generateHashCode() + this.values.hashCode();
   }

   public static LongProperty create(String pName, long pMin, long pMax) {
      return new LongProperty(pName, pMin, pMax);
   }

   public Optional<Long> getValue(String pValue) {
      try {
         Long integer = Long.valueOf(pValue);
         return this.values.contains(integer) ? Optional.of(integer) : Optional.empty();
      } catch (NumberFormatException numberformatexception) {
         return Optional.empty();
      }
   }

   /**
    * @return the name for the given value.
    */
   public String getName(Long pValue) {
      return pValue.toString();
   }
}