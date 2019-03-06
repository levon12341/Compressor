package main;

import exceptions.NoTableFoundException;
import exceptions.NoFileFoundException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import java.awt.event.*;
import java.awt.*;

import java.io.IOException;

public class Frame extends JFrame {
    private JButton compressButton;
    private JButton extractButton;
    private JTextArea extractTextArea;
    private JTextArea compressTextArea;
    private JScrollBar scrollBar;
    static JProgressBar progressBar;

    public Frame(String name) {
		super(name);
		init();
    }

    public void init() {
		this.setLayout(new FlowLayout());
		this.setSize(300, 200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		compressButton = new JButton("Сжать");
		extractButton = new JButton("Извлечь");


		compressTextArea = new JTextArea(1, 20); 
		compressTextArea.setEditable(true);
		extractTextArea = new JTextArea(1, 20);
		extractTextArea.setEditable(true);

		scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollBar.add(compressTextArea);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
        JFrame jfrm = this; 
		
		compressButton.addActionListener(ae -> {
            progressBar.setValue(0);
            try {
               	String s = "";
            	for (String line : Runner.startProc("compress", compressTextArea.getText())) {
               		if (line != null)
               			s += line + '\n';
            		else {
                   		JOptionPane.showMessageDialog(jfrm, s);
          			}
       			}
       			progressBar.setValue(100);
       			JOptionPane.showMessageDialog(jfrm, s);
       		} catch (IOException e) {
       			JOptionPane.showMessageDialog(jfrm, "Некорректный ввод");
       		} catch (NullPointerException ignore) {
           		ignore.printStackTrace();
       		}
		});

		extractButton.addActionListener(ae -> {
			progressBar.setValue(0);
			try {
				String s = "";
				for (String line : Runner.startProc("extract", extractTextArea.getText())) {
               		if (line != null)
						s += line + '\n';
					else {
						JOptionPane.showMessageDialog(jfrm, s);
					}
				}
				JOptionPane.showMessageDialog(jfrm, s);
				} catch (NoTableFoundException noTableE) {
					JOptionPane.showMessageDialog(jfrm, noTableE.getMessage());
				} catch (NoFileFoundException e) {
					JOptionPane.showMessageDialog(jfrm, e.getMessage());
				} catch (NullPointerException ignore) {
					ignore.printStackTrace();
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (IOException e){
					e.printStackTrace();
				}
		});	
		 
		this.add(compressTextArea);
		this.add(compressButton);
		this.add(extractTextArea);
		this.add(extractButton);
		this.add(progressBar);
		this.add(scrollBar);
		this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}