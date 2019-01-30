package com.company;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

import static java.awt.Frame.MAXIMIZED_BOTH;

public class Main {

    public static void main(String[] args) {
        // Creating the Frame
        JFrame frame = new JFrame("Поисковик Павла");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);;


        //Creating the panels at the top
        DefaultListModel listModel = new DefaultListModel();
        JList list = new JList(listModel);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2,5,5));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout (0,2,5,5));
        JLabel label = new JLabel("Введи расширение файла");
        JLabel label2 = new JLabel("Введи искомый текст");
        JTextField tf1= new JTextField(10);
        JTextField tf2 = new JTextField(20);
        JButton patchDir = new JButton("Выбрать директорию поиска");
        patchDir.setEnabled(false);
        tf2.setEnabled(false);
        tf1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() > 0) {
                    tf2.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0) {
                    tf2.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        tf2.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (e.getDocument().getLength()>0){
                    patchDir.setEnabled(true);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (e.getDocument().getLength() == 0){
                patchDir.setEnabled(false);}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        JTextArea textArea2 = new JTextArea(8,10);
        textArea2.setLineWrap(true);
        textArea2.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea2);
        JScrollPane scroll2 = new JScrollPane(list);


        //JScrollPane scrollPane = new JScrollPane(textArea1);
        panel.add(label);
        panel.add(tf1);
        panel.add(label2);
        panel.add(tf2);
        panel.add(patchDir);
        panel1.add(scroll2);
        panel1.add(scroll);

                // Creating FileChooser
        JFileChooser fileChooser = new JFileChooser();
        patchDir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор директории");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String directory = file.getAbsolutePath();// the file is the directory
                    String nameToFind = tf1.getText();
                    String partOfContent = tf2.getText();
                    listModel.removeAllElements();
                    textArea2.setText(null);
                    // use your algorithm to find the file
                    try {
                        Files.walkFileTree(Paths.get(directory), new SimpleFileVisitor<Path>(){
                            // не могу разобраться с этим методом!!!
                            @Override
                            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                                /*  по идее я тут должен написать */
                                String content = new String(Files.readAllBytes(file));
                                                                boolean containsContent = true;

                                if(partOfContent.toLowerCase()!=null && !content.toLowerCase().contains( partOfContent.toLowerCase())) {
                                    containsContent = false;
                                }
                                if(file.toString().endsWith(nameToFind) && containsContent ){
                                    listModel.addElement(file.toString());
                                    System.out.println(list.toString());
                                    return FileVisitResult.CONTINUE;
                                }

                                return FileVisitResult.CONTINUE;
                            }
                        });
                    } catch (IOException P) {
                        P.printStackTrace();
                    }
                }
            }
        });

        //add SelectionListener to Jlist
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex()>=0) {
                    System.out.println("ok");
                        try {
                            Scanner in = new Scanner(new File(list.getSelectedValue().toString()));
                            StringBuffer data = new StringBuffer();
                            while (in.hasNext())
                                data.append(in.nextLine()).append("\n");
                            textArea2.setText(data.toString());
                        } catch ( Exception ex ) {
                            ex.printStackTrace();
                        }
                }
            }
        });




                //Adding components to a frame
        frame.getContentPane().add(BorderLayout.NORTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER,panel1);
        frame.setVisible(true);
        frame.setExtendedState(MAXIMIZED_BOTH);
    }
}
