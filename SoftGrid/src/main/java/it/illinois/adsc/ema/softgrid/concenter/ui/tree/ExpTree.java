/* Copyright (C) 2016 Advanced Digital Science Centre

        * This file is part of Soft-Grid.
        * For more information visit https://www.illinois.adsc.com.sg/cybersage/
        *
        * Soft-Grid is free software: you can redistribute it and/or modify
        * it under the terms of the GNU General Public License as published by
        * the Free Software Foundation, either version 3 of the License, or
        * (at your option) any later version.
        *
        * Soft-Grid is distributed in the hope that it will be useful,
        * but WITHOUT ANY WARRANTY; without even the implied warranty of
        * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        * GNU General Public License for more details.
        *
        * You should have received a copy of the GNU General Public License
        * along with Soft-Grid.  If not, see <http://www.gnu.org/licenses/>.

        * @author Prageeth Mahendra Gunathilaka
*/
package it.illinois.adsc.ema.softgrid.concenter.ui.tree;

import it.illinois.adsc.ema.softgrid.concenter.Experiment;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by prageethmahendra on 12/9/2016.
 */
public class ExpTree extends JPanel implements TreeSelectionListener {
    private JTree tree;
    private DefaultTreeModel treeModel;
    private Experiment experiment;
    private ArrayList<ActionListener> actionListenerList = new ArrayList<ActionListener>();
    private boolean selected;
    private DefaultMutableTreeNode root;

    public ExpTree(Experiment experiment) {
        try {
            this.experiment = experiment;
            setupGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupGUI() throws Exception {
        this.setLayout(new BorderLayout());
        root = new DefaultMutableTreeNode(experiment.getName());
        File expDir = new File(experiment.getConfigPath());
        if(!expDir.exists())
        {
            try {
                expDir.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Experiment Folder = " + expDir.getAbsolutePath());
        for (File file : expDir.listFiles()) {
            root.add(new DefaultMutableTreeNode(file.getName()));
        }
        treeModel = new CustomTreeModel(root);
        tree = new JTree(treeModel);
        this.add(tree, BorderLayout.CENTER);
        tree.addTreeSelectionListener(this);
    }

    public JTree getTree() {
        return tree;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new ExpTree(new Experiment("Dummy", "Dummy Path")), BorderLayout.CENTER);
        frame.setSize(new Dimension(500, 500));
        frame.setVisible(true);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
                .getPath().getLastPathComponent();
        if (node != null) {
            String s = node.getUserObject().toString();
            fireActionPerformed();
        }
    }

    private void fireActionPerformed() {
        for (ActionListener actionListener : actionListenerList) {
            actionListener.actionPerformed(new ActionEvent(this, 0, experiment.getName()));
        }
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListenerList.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.actionListenerList.remove(actionListener);
    }


    public void setSelected(boolean selected) {
        if (!selected) {
            tree.clearSelection();
        } else {
            tree.setSelectionRow(0);
        }
    }
}
