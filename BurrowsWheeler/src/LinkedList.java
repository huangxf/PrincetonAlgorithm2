/**
 * Created by Administrator on 13-12-29.
 */
public class LinkedList {
    private class Node {
        public int value = 0;
        public Node prev = null;
        public Node next = null;
        public Node(int value, Node next){
            this.value = value;
            this.next = next;
        }
    }

    private Node head = null;
    private int lastFind = 0;


    public LinkedList() {
        head = new Node(-1,null);

        head.prev = head;

        Node prevNode = head;
        Node curNode = null;
        for(int i = 0; i < 256; i++) {
            curNode = new Node(i,null);
            curNode.prev = prevNode;
            curNode.prev.next = curNode;
            prevNode = curNode;
            curNode = curNode.next;
        }

    }

    public int findByStep(int step) {
        Node node = head.next;
        for(int i = 0; i < step; i++) {
            node = node.next;
        }
        if(node != null) return node.value;
        else return -1;
    }


    public Node find(int value) {
        lastFind = 0;
        Node found = head;
        while(found != null) {
            lastFind++;
            if(found.value == value) return found;
            else found = found.next;
        }
        lastFind = 0;
        return null;
    }

    public void moveToFirst(int value) {
        if(head.value == value) return;
        Node node = find(value);

        if(node == null) return;

        if(head.next == null) {
            head.next = node;
            node.prev = head;
            return;
        }

        if(node.next == null) {
            node.prev.next = null;
            node.next = head.next;
            head.next.prev = node;
            node.prev = head;
            head.next = node;
            return;
        }

        if(node != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.next = head.next;
            head.next.prev = node;
            node.prev = head;
            head.next = node;
        }
    }

    public void display() {
        for(Node node = head; node != null; node = node.next) {
            System.out.print(node.value);
            System.out.print(" ");
        }
        System.out.println("");
    }

    public int getLastFind() {
        if(lastFind != 0)
            return lastFind - 2;
        else
            return 0;
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.moveToFirst(5);
        list.display();
    }

}
