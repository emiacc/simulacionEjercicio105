package clases;

import java.util.NoSuchElementException;
public class SimpleList
{
      private Node frente;
      private Node actual; 
      private int size;
      private final int capacidad;
      
      public SimpleList (int capacidad)
      {
          frente = null;
          actual = null;
          size = 0;
          this.capacidad = capacidad;
      }
      
      public boolean addInOrder(Comparable x)
      {
          if(size < capacidad){
            Node nuevo = new Node( x, null );
            Node p = frente, q = null;
            while ( p != null && x.compareTo( p.getInfo() ) >= 0 )
            {
                q = p;
                p = p.getNext();  
            }
            nuevo.setNext( p );
            if( q != null ) q.setNext( nuevo );
            else frente = nuevo;   
            size++;
            return true;
          }
          else
              return false;
      }  
      
      public void clear( )
      {
         frente = null; 
         actual = null;
         size = 0;
      }
      
      public boolean hasNext()
      {
         if ( frente == null ) return false;
         if ( actual != null && actual.getNext() == null ) return false;
         return true;
      }
      
      public boolean isEmpty()
      {
         return (frente == null);    
      }
      
      public Comparable next()
      {
          if ( ! hasNext() ) throw new NoSuchElementException("No quedan elementos por recorrer");
          
          if ( actual == null ) actual = frente;
          else actual = actual.getNext();
          return actual.getInfo();
      }
      
      public Comparable removeLast()
      {
         if (frente == null) throw new NoSuchElementException("Error: la lista esta vacia...");
         Node p = frente, q = null;
         while( p.getNext() != null )
         {
            q = p;
            p = p.getNext();
         }
         Comparable x = p.getInfo();
         if( q != null ) q.setNext( p.getNext() );
         else frente = p.getNext();
         size--;
         return x;
      }
      public Comparable getLast()
      {
         if (frente == null) throw new NoSuchElementException("Error: la lista esta vacia...");
         
         Node p = frente, q = null;
         while( p != null )
         {
            q = p;
            p = p.getNext();
         }
         return ( q != null )? q.getInfo() : frente.getInfo();
      }
      public void startIterator()
      {
            actual = null;    
      }
      
      public int size()
      {
          return size;
      }
      
      @Override
      public String toString()
      {
             Node p = frente;
             String res = "[ ";
             while( p != null )
             {
                res = res + p.toString();
                if ( p.getNext() != null ) res = res + " -|- ";
                p = p.getNext();
             }
             res = res + " ]";
             return res;
      }   
}
