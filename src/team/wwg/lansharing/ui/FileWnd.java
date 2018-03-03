package team.wwg.lansharing.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;  
import javax.swing.tree.*;  
import javax.swing.event.*;  
public class FileWnd extends JFrame implements TreeSelectionListener  
{  
    private JLabel label;  
  
    void CreatTree(String FlieInfo,int level,DefaultTreeModel treeModel,DefaultMutableTreeNode root)
    {
    		Scanner scan = new Scanner(FlieInfo);
    		int FlieLevel = scan.nextInt();
    		if(FlieLevel==level)
    		{
    			DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(scan.nextLine());
    			treeModel.insertNodeInto(node1,root,root.getChildCount());
    		}
    		else if(FlieLevel>level)
    		{
    			level+=1;
    			CreatTree(FlieInfo,level,treeModel,(DefaultMutableTreeNode)root.getLastChild());
    			level-=1;
    		}
    		else if(FlieLevel<level)
    		{
    			level-=1;
    			CreatTree(FlieInfo,level,treeModel,(DefaultMutableTreeNode)root.getParent());
    			level+=1;
    		}
    }
    
    public FileWnd(ArrayList<String> FileList)  
    {  
    	
    	
        super("树形菜单");  setSize(200,400);  
        Container container = getContentPane();  
        int level=0;
        //创建根节点和子节点  
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("分享文件");  
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        for(int i = 0 ;i<FileList.size();i++)
        {
        	CreatTree(FileList.get(i),level,treeModel,root);
        	
        }

        //创建树对象  
        JTree tree = new JTree(treeModel);  
        //设置Tree的选择为一次只能选择一个节点  
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);  
        //注册监听器  
        tree.addTreeSelectionListener(this);  
  
        tree.setRowHeight(20);  
  
        //创建节点绘制对象  
        DefaultTreeCellRenderer cellRenderer =  
                            (DefaultTreeCellRenderer)tree.getCellRenderer();  
  
        //设置字体  
        cellRenderer.setFont(new Font("Serif",Font.PLAIN,14));  
        cellRenderer.setBackgroundNonSelectionColor(Color.white);  
        cellRenderer.setBackgroundSelectionColor(Color.yellow);  
        cellRenderer.setBorderSelectionColor(Color.red);  
  
        //设置选或不选时，文字的变化颜色  
        cellRenderer.setTextNonSelectionColor(Color.black);  
        cellRenderer.setTextSelectionColor(Color.blue);  
          
        //把树对象添加到内容面板  
        container.add(new JScrollPane(tree));  
  
        //创建标签  
        label = new JLabel("你当前选择的节点为：",JLabel.CENTER);  
        label.setFont(new Font("Serif",Font.PLAIN,14));  
        container.add(label,BorderLayout.SOUTH);  
  
        setVisible(true);   //设置可见  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口关闭动作  
    }  
  
    //处理TreeSelectionEvent事件  
    public void valueChanged(TreeSelectionEvent event)  
    {  
        JTree tree = (JTree)event.getSource();  
        //获取目前选取的节点  
        DefaultMutableTreeNode selectionNode =(DefaultMutableTreeNode)tree.getLastSelectedPathComponent();  
        String nodeName = selectionNode.toString();  
        label.setText("你当前选取的节点为： "+nodeName);  
    }
  
}  
