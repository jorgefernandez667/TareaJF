package com.mycompany.tareasjf;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class TareasJF extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskField;
    private JButton addButton;
    private Image backgroundImage;

    public TareasJF() {
        setTitle("Lista de Tareas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);

        
        
        try {
            File iconFile = new File("bloc.png");
            if (iconFile.exists()) {
                Image icon = new ImageIcon(iconFile.getAbsolutePath()).getImage();
                setIconImage(icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            backgroundImage = new ImageIcon("libreta2.jpg").getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setCellRenderer(new TaskListCellRenderer());
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    int index = taskList.locationToIndex(e.getPoint());
                    Task task = taskListModel.getElementAt(index);
                    task.setCompleted(!task.isCompleted());
                    taskList.repaint();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        taskField = new JTextField();
        addButton = new JButton("Agregar tarea");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        JButton deleteButton = new JButton("Eliminar tarea");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    taskListModel.remove(selectedIndex);
                }
            }
        });
        add(deleteButton, BorderLayout.NORTH);

       
        taskField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addButton.doClick();
            }
        });
    }
 @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
    private void addTask() {
        String taskName = taskField.getText();
        if (!taskName.isEmpty()) {
            Task task = new Task(taskName, false);
            taskListModel.addElement(task);
            taskField.setText("");
        }
    }

    private static class Task {
        private String name;
        private boolean completed;

        public Task(String name, boolean completed) {
            this.name = name;
            this.completed = completed;
        }

        public String getName() {
            return name;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getStatus() {
            return completed ? "completo" : "incompleto";
        }
    }

    private static class TaskListCellRenderer extends JPanel implements ListCellRenderer<Task> {
        private JCheckBox checkBox;
        private JLabel label;

        public TaskListCellRenderer() {
            setLayout(new BorderLayout());
            checkBox = new JCheckBox();
            label = new JLabel();
            add(checkBox, BorderLayout.WEST);
            add(label, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index, boolean isSelected, boolean cellHasFocus) {
            checkBox.setSelected(task.isCompleted());
            label.setText(task.getName() + " (" + task.getStatus() + ")");
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TareasJF app = new TareasJF();
                app.setVisible(true);
            }
        });
    }
}