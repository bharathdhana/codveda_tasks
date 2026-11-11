class TreeNode {
    int data;
    TreeNode left;
    TreeNode right;

    TreeNode(int data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

public class BinarySearchTree {
    private TreeNode root;

    public BinarySearchTree() {
        root = null;
    }

    public void insert(int data) {
        root = insertRec(root, data);
    }

    private TreeNode insertRec(TreeNode node, int data) {
        if (node == null) {
            return new TreeNode(data);
        }

        if(data < node.data) {
            node.left = insertRec(node.left, data);
        } else {
            node.right = insertRec(node.right, data);
        }
        return node;
    }

    public boolean search(int data) {
        return searchRec(root, data);
    }

    public boolean searchRec(TreeNode node, int data) {
        if (node == null) {
            return false;
        }

        if(data == node.data) {
            return true;
        }
        return data < node.data ? searchRec(node.left, data) : searchRec(node.right, data);
    }

    public void delete(int data) {
        root = deleteRec(root, data);
    }

    private TreeNode deleteRec(TreeNode node, int data) {
        if (node == null) {
            return null;
        }

        if (data < node.data) {
            node.left = deleteRec(node.left, data);
        } else if(data > node.data) {
            node.right = deleteRec(node.right, data);
        } else {
            if (node.left == null) {
                return node.right;
            }
            if(node.right == null) {
                return node.left;
            }
            node.data = minValue(node.right);
            node.right = deleteRec(node.right, node.data);
        }
        return node;
    }

    private int minValue(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node.data;
    }

    public void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    public void inOrderRec(TreeNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.data + " ");
            inOrderRec(node.right);
        }
    }

    public void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    public void preOrderRec(TreeNode node) {
        if(node != null) {
            System.out.print(node.data + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    public void postOrder() {
        postOrderRec(root);
        System.out.println();
    }

    public void postOrderRec(TreeNode node) {
        if(node != null) {
            postOrderRec(node.left);
            postOrderRec(node.right);
            System.out.print(node.data + " ");
        }
    }

    static void main(String[] args) {
        BinarySearchTree bst = new BinarySearchTree();
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int val : values) {
            bst.insert(val);
        }

        System.out.print("Inorder: ");
        bst.inOrder();
        System.out.print("Preorder: ");
        bst.preOrder();
        System.out.print("Postorder: ");
        bst.postOrder();

        System.out.println("\nSearch 40: " +  bst.search(40));
        System.out.println("\nSearch 99: " +  bst.search(99));

        System.out.println("\nDeleting 20 (leaf)");
        bst.delete(20);
        System.out.println("Inorder: ");
        bst.inOrder();

        System.out.println("Deleting 30 (one child)");
        bst.delete(30);
        System.out.println("Inorder: ");
        bst.inOrder();

        System.out.println("Deleting 50 (two children, root)");
        bst.delete(50);
        System.out.println("Inorder: ");
        bst.inOrder();
        System.out.println("Preorder: ");
        bst.preOrder();
        System.out.println("Postorder: ");
        bst.postOrder();
    }
}
