package demo.bean;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("serial")
public class NodeList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Queue<E>, Cloneable,
        Serializable {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Node<E> first;

    private Node<E> last;

    private int size;

    public NodeList() {
    }

    private void linkFirst(Node<E> node) {
        lock.writeLock().lock();
        if (first == null) {
            last = node;
        } else {
            // 插入的节点的后继元素为修改前的第一个
            node.setNext(first);
            // 修改前第一个元素的前驱元素为插入的节点
            first.setPrev(node);
        }
        // 将链表存储的第一个变量修改为插入的元素
        first = node;
        size++;
        lock.writeLock().unlock();
    }

    private void linkLast(Node<E> node) {
        lock.writeLock().lock();
        if (last == null) {
            first = node;
        } else {
            // 插入的节点的前驱元素为修改前的最后一个
            node.setPrev(last);
            // 修改前最后一个元素的后继元素为插入的节点
            last.setNext(node);
        }
        // 将链表存储的最后一个变量修改为插入的元素
        last = node;
        size++;
        lock.writeLock().unlock();
    }

    /**
     * 移除节点
     */
    private E unlink(Node<E> node) {
        // assert node != null;
        lock.writeLock().lock();
        E element = node.getData();
        Node<E> next = node.getNext();
        Node<E> prev = node.getPrev();
        synchronized (this) {
            if (prev == null) {
                first = next;
            } else {
                prev.setNext(next);
                node.setPrev(null);
            }
            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
                node.setNext(null);
            }
            node.setData(null);
            size--;
        }
        lock.writeLock().unlock();
        return element;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        lock.writeLock().lock();
        Node<E> node = node(index);
        Node<E> prev = node.getPrev();
        Node<E> newNode = new Node<E>(prev, element, node);
        if (prev == null) {
            first = newNode;
        } else {
            node.setPrev(newNode);
            prev.setNext(newNode);
        }
        size++;
        lock.writeLock().unlock();
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        for (Node<E> node = first; node != null; ) {
            Node<E> next = node.getNext();
            node.setData(null);
            node.setNext(null);
            node.setPrev(null);
            node = next;
        }
        first = last = null;
        size = 0;
        lock.writeLock().unlock();
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    /**
     * 获取指定下标的节点
     */
    public Node<E> node(int index) {
        checkIndex(index);
        lock.readLock().lock();
        if (index < size >> 1) {
            Node<E> node = first;
            for (int i = 0; i < index; i++) {
                node = node.getNext();
            }
            lock.readLock().unlock();
            return node;
        } else {
            Node<E> node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.getPrev();
            }
            lock.readLock().unlock();
            return node;
        }
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        lock.readLock().lock();
        if (o == null) {
            for (Node<E> node = first; node != null; node = node.getNext()) {
                if (node.getData() == null) {
                    lock.readLock().unlock();
                    return index;
                }
                index++;
            }
        } else {
            for (Node<E> node = first; node != null; node = node.getNext()) {
                if (o.equals(node.getData())) {
                    lock.readLock().unlock();
                    return index;
                }
                index++;
            }
        }
        lock.readLock().unlock();
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int index = size;
        lock.readLock().lock();
        if (o == null) {
            for (Node<E> node = last; node != null; node = node.getPrev()) {
                index--;
                if (node.getData() == null) {
                    lock.readLock().unlock();
                    return index;
                }
            }
        } else {
            for (Node<E> node = last; node != null; node = node.getPrev()) {
                index--;
                if (o.equals(node.getData())) {
                    lock.readLock().unlock();
                    return index;
                }
            }
        }
        lock.readLock().unlock();
        return -1;
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        lock.writeLock().lock();
        Node<E> node = node(index);
        E old = node.getData();
        node.setData(element);
        lock.writeLock().unlock();
        return old;
    }

    /**
     * 移除首节点
     */
    private E unlinkFirst(Node<E> node) {
        // assert node == first && node != null;
        lock.writeLock().lock();
        final E element = node.getData();
        final Node<E> next = node.getNext();
        node.setData(null);
        node.setNext(null); // help GC
        first = next;
        if (next == null) {
            last = null;
        } else {
            next.setPrev(null);
        }
        size--;
        lock.writeLock().unlock();
        return element;
    }

    /**
     * 移除尾节点
     */
    private E unlinkLast(Node<E> node) {
        // assert node == last && node != null;
        lock.writeLock().lock();
        final E element = node.getData();
        final Node<E> prev = node.getPrev();
        node.setData(null);
        node.setPrev(null); // help GC
        last = prev;
        if (prev == null) {
            first = null;
        } else {
            prev.setNext(null);
        }
        size--;
        lock.writeLock().unlock();
        return element;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        }
        if (index == size - 1) {
            return removeLast();
        }
        return unlink(node(index));
    }

    @Override
    public boolean remove(Object o) {
        lock.readLock().lock();
        if (o == null) {
            for (Node<E> node = first; node != null; node = node.getNext()) {
                if (node.getData() == null) {
                    lock.readLock().unlock();
                    unlink(node);
                    return true;
                }
            }
        } else {
            for (Node<E> node = first; node != null; node = node.getNext()) {
                if (o.equals(node.getData())) {
                    lock.readLock().unlock();
                    unlink(node);
                    return true;
                }
            }
        }
        lock.readLock().unlock();
        return false;
    }

    /**
     * 移除第一个元素
     */
    @Override
    public E removeFirst() {
        return unlinkFirst(first);
    }

    /**
     * 移除最后一个元素
     */
    @Override
    public E removeLast() {
        return unlinkLast(last);
    }

    /**
     * 从此双端队列移除第一次出现的指定元素。如果此双端队列不包含该元素，则不作更改。更确切地讲，移除第一个满足 (o==null ? e==null :
     * o.equals(e)) 的元素 e（如果存在这样的元素）。如果此双端队列包含指定的元素（或者此双端队列由于调用而发生了更改），则返回 true。
     */
    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    /**
     * 从此双端队列移除最后一次出现的指定元素。如果此双端队列不包含该元素，则不作更改。更确切地讲，移除最后一个满足 (o==null ? e==null :
     * o.equals(e)) 的元素 e（如果存在这样的元素）。如果此双端队列包含指定的元素（或者此双端队列由于调用而发生了更改），则返回 true。
     */
    @Override
    public boolean removeLastOccurrence(Object o) {
        lock.readLock().lock();
        if (o == null) {
            for (Node<E> node = last; node != null; node = node.getPrev()) {
                if (node.getData() == null) {
                    lock.readLock().unlock();
                    unlink(node);
                    return true;
                }
            }
        } else {
            for (Node<E> node = last; node != null; node = node.getPrev()) {
                if (o.equals(node.getData())) {
                    lock.readLock().unlock();
                    unlink(node);
                    return true;
                }
            }
        }
        lock.readLock().unlock();
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void addFirst(E e) {
        linkFirst(new Node<>(null, e, null));
    }

    @Override
    public void addLast(E e) {
        linkLast(new Node<>(null, e, null));
    }

    /**
     * 下标越界检查
     */
    public void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        // TODO 自动生成的方法存根
        return null;
    }

    /**
     * 获取，但不移除此双端队列所表示的队列的头部（换句话说，此双端队列的第一个元素）。此方法与 peek
     * 唯一的不同在于：如果此双端队列为空，它将抛出一个异常。
     */
    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E get(int index) {
        return node(index).getData();
    }

    @Override
    public E getFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        return first.getData();
    }

    @Override
    public E getLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last.getData();
    }

    /**
     * 将指定元素插入此双端队列所表示的队列（换句话说，此双端队列的尾部），如果可以直接这样做而不违反容量限制的话；如果成功，则返回
     * true，如果当前没有可用的空间，则返回 false
     */
    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    /**
     * 在不违反容量限制的情况下，将指定的元素插入此双端队列的开头
     */
    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    /**
     * 在不违反容量限制的情况下，将指定的元素插入此双端队列的末尾
     */
    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    /**
     * 获取，但不移除此双端队列所表示的队列的头部（换句话说，此双端队列的第一个元素）；如果此双端队列为空，则返回 null
     */
    @Override
    public E peek() {
        return peekFirst();
    }

    /**
     * 获取，但不移除此双端队列的第一个元素；如果此双端队列为空，则返回 null
     */
    @Override
    public E peekFirst() {
        if (first != null) {
            return first.getData();
        }
        return null;
    }

    /**
     * 获取，但不移除此双端队列的最后一个元素；如果此双端队列为空，则返回 null
     */
    @Override
    public E peekLast() {
        if (last != null) {
            return last.getData();
        }
        return null;
    }

    /**
     * 从此双端队列所表示的堆栈中弹出一个元素。换句话说，移除并返回此双端队列第一个元素。
     */
    @Override
    public E poll() {
        return removeFirst();
    }

    /**
     * 获取并移除此双端队列的第一个元素；如果此双端队列为空，则返回 null
     */
    @Override
    public E pollFirst() {
        return removeFirst();
    }

    /**
     * 获取并移除此双端队列的最后一个元素；如果此双端队列为空，则返回 null
     */
    @Override
    public E pollLast() {
        return removeLast();
    }

    /**
     * 从此双端队列所表示的堆栈中弹出一个元素。换句话说，移除并返回此双端队列第一个元素。
     */
    @Override
    public E pop() {
        return removeFirst();
    }

    /**
     * 将一个元素推入此双端队列所表示的堆栈（换句话说，此双端队列的头部），如果可以直接这样做而不违反容量限制的话；如果成功，则返回
     * true，如果当前没有可用空间，则抛出 IllegalStateException。
     */
    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E remove() {
        return removeLast();
    }

    @Override
    public String toString() {
        if (first != null) {
            StringBuilder sb = new StringBuilder();
            Node<E> node = first;
            sb.append("NodeList {");
            while (node != null) {
                sb.append(node.toString());
                node = node.getNext();
                if (node != null) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        }
        return "NodeList { }";
    }

}
