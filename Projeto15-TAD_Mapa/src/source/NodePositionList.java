package source;

import java.util.Iterator;

import exceptions.BoundaryViolationException;
import exceptions.EmptyListException;
import exceptions.InvalidPositionException;
import interfaces.Position;
import interfaces.PositionList;

/**
 * @author Geovane Donizete Laera  - RA: 1902679 
 * @author Isaque Ribeiro dos Santos Junior - RA: 1903978
 * @author Marcelo Martinez Mesa Campos - RA: 1905076 
 * @author Paulo Ricardo Costa da Silva - RA: 1905013 
 * @author Vinícius da Cruz Pera - RA: 1903144
 * Data: 27/10/2021
 *
 */
public class NodePositionList<E> implements PositionList<E> {
	// Número de elementos na lista
	protected int numElts;
	
	// Sentinelas especiais
	protected DNode<E> header, trailer;

	// Método construtor
	public NodePositionList() {
		numElts = 0;
		header = new DNode<E>(null, null, null);
		trailer = new DNode<E>(header, null, null);
		header.setNext(trailer); 
	}

	// Verifica se a posição é válida para esta lista e a converte para DNode se for válida
	protected DNode<E> checkPosition(Position<E> p) throws InvalidPositionException {
		if (p == null) {
			throw new InvalidPositionException("Null position passed to NodeList");
		}
		
		if (p == header) {
			throw new InvalidPositionException("The header node is not a valid position");
		}
		
		if (p == trailer) {
			throw new InvalidPositionException("The trailer node is not a valid position");
		}
		
		try {
			DNode<E> temp = (DNode<E>) p;
			
			if ((temp.getPrev() == null) || (temp.getNext() == null)) {
				throw new InvalidPositionException("Position does not belong to a valid NodeList");
			}
			
			return temp;
		} 
		catch (ClassCastException e) {
			throw new InvalidPositionException("Position is of wrong type for this list");
		}
	}
	
	// Retorna a quantidade de elementos na lista
	public int size() {
		return numElts;
	}

	// Retorna quando a lista esta vazia
	public boolean isEmpty() { 
		return (numElts == 0); 
	}
	
	// Retorna a primeira posição da lista
	public Position<E> first() throws EmptyListException {
		if (isEmpty()) {
			throw new EmptyListException("List is empty");
		}
		
		return header.getNext();
	}

	// Retorna a posição que antecede a fornecida
	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNode<E> v = checkPosition(p);
		DNode<E> prev = v.getPrev();
		if (prev == header) {
			throw new BoundaryViolationException("Cannot advance past the beginning of the list");
		}
		
		return prev;
	}

	// Insere o elemento antes da posição fornecida, retornando a nova posição
	public void addBefore(Position<E> p, E element) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElts++;
		DNode<E> newNode = new DNode<E>(v.getPrev(), v, element);
		v.getPrev().setNext(newNode);
		v.setPrev(newNode);
	}

	// Insere o elemento dado no início da lista, retornando a nova posição
	public void addFirst(E element) {
		numElts++;
		DNode<E> newNode = new DNode<E>(header, header.getNext(), element);
		header.getNext().setPrev(newNode);
		header.setNext(newNode);
	}
	
	// Remove da lista a posição fornecida
	public E remove(Position<E> p) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElts--;
		DNode<E> vPrev = v.getPrev();
		DNode<E> vNext = v.getNext();
		vPrev.setNext(vNext);
		vNext.setPrev(vPrev);
		E vElem = v.element();

		v.setNext(null);
		v.setPrev(null);
		return vElem;
	}

	// Substitui o elemento da posição fornecida por um novo e retorna o elemento velho
	public E set(Position<E> p, E element) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		E oldElt = v.element();
		v.setElement(element);
		return oldElt;
	}

	// Retorna o último nodo da lista.
	public Position<E> last() {
		if (isEmpty()) {
			throw new EmptyListException("List is empty");
		}
		
		return trailer.getPrev();
	}

	// Retorna o nodo que segue um dado nodo da lista.
	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNode<E> v = checkPosition(p);
		DNode<E> next = v.getNext();
		if (next == trailer) {
			throw new BoundaryViolationException("Cannot advance past the finaling of the list");
		}
		
		return next;
	}
	
	// Insere um elemento na última posição, retornando uma posição nova.
	public void addLast(E e) {
		numElts++;
		DNode<E> newNode = new DNode<E>(trailer.getPrev(), trailer, e);
		trailer.getPrev().setNext(newNode);
		trailer.setPrev(newNode);
	}

	// Insere um elemento após um dado elemento da lista.
	public void addAfter(Position<E> p, E e) throws InvalidPositionException {
		DNode<E> v = checkPosition(p);
		numElts++;
		DNode<E> newNode = new DNode<E>(v, v.getNext(), e);
		v.getNext().setPrev(newNode);
		v.setNext(newNode);
	}
	
	// Move um elemento para a primeira posição
	public void makeFirst(Position<E> position) {
		DNode<E> validaPosition = checkPosition(position);

		DNode<E> validaPositionPrev = validaPosition.getPrev();
		DNode<E> validaPositionNext = validaPosition.getNext();
		validaPositionPrev.setNext(validaPositionNext); 
		validaPositionNext.setPrev(validaPositionPrev); 
		
		validaPosition.setNext(header.getNext()); 
		validaPosition.setPrev(header); 
		
		header.getNext().setPrev(validaPosition); 
		header.setNext(validaPosition); 		
	}

	// Verifica uma posição na lista
	public boolean checkPositionList(Position<E> p) {
		DNode<E> recebido = checkPosition(p);
		DNode<E> primeiro = header.getNext();

		for (int i = 0; i < numElts; i++) {
			if (recebido.element() == primeiro.element()) {
				if (recebido.getPrev() == primeiro.getPrev() && recebido.getNext() == recebido.getNext()) {
					return true;
				}
			}

			primeiro = primeiro.getNext();
		}

		return false;
	}
	
	// Método estático que converte a lista para String
	public static <E> String toString(PositionList<E> l) {
		String s = "";
		for (E i: l) { 
			s += ", " + i; 
		}
		
		s = (s.length() == 0 ? s : s.substring(2));
		return "[" + s + "]";
	}

	// Retorna o iterator a partir do ElemenIterator.
	public Iterator<E> iterator() { 
		return new ElementIterator<E>(this); 
	}
	
	// Método que a lista em forma de String
	public String toString() { 
		return toString(this); 
	}
}