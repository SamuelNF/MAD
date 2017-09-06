import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.PopupWindow;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import java.io.*;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.stage.*;

public class Window extends Application 
{
   static GridPane root = new GridPane();
   static Framework framework = new Framework();
   static TabPane tabPane = new TabPane();

   public static void main(String[] args)
   {
      launch(args);
   }

   @Override
   public void start(Stage primaryStage)
   {
      primaryStage.setTitle("MAD");
      setupLayout(root);
      primaryStage.setScene(new Scene(root, 1400, 700));
      primaryStage.show();
   }

   private void setupLayout(GridPane root)
   {
      Menu moduleMenu = setupModuleMenu();
      Menu fileMenu = setupFileMenu();

      MenuBar menuBar = new MenuBar();
      menuBar.getMenus().addAll(moduleMenu, fileMenu);
      menuBar.setMinWidth(1400);

      root.add(menuBar,0,0);
      root.add(tabPane,0,1);
      addModuleTab("Test");
   }

   private VBox createAssumptionTable(UIModule... module)
   {
      TableView table = new TableView();
      table.setEditable(true);
      TableColumn firstCol = new TableColumn("Assumption");
      TableColumn lastCol = new TableColumn("Contrary");
      firstCol.setCellValueFactory(
         new PropertyValueFactory<Assumption, String>("assumption"));
      lastCol.setCellValueFactory(
         new PropertyValueFactory<Assumption, String>("contrary"));
      table.getColumns().addAll(firstCol, lastCol);

      Label tablelabel = new Label("Assumptions");
      tablelabel.setFont(new Font("Arial", 20));
      Label createlabel = new Label("Add Assumption");
      createlabel.setFont(new Font("Arial", 20));

      TextField assField = new TextField("assumption");
      TextField conField = new TextField("contrary");

      ObservableList<Assumption> list = FXCollections.observableArrayList();
      table.setItems(list);
      if(module!=null && module.length>0)
      {
         for(Assumption a :module[0].getAssumptions())
         {
            list.add(a);
         }
      }

      Button createbtn = new Button("Create Assumption");
      createbtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            addAssumption(list,assField.getText(),conField.getText());
         }
      });

      HBox hbox1 = new HBox();
      hbox1.setSpacing(5);
      hbox1.setPadding(new Insets(10, 0, 0, 10));
      hbox1.getChildren().addAll(assField,conField);
      
      HBox hbox2 = new HBox();
      hbox2.setSpacing(5);
      hbox2.setPadding(new Insets(10, 0, 20, 10));
      hbox2.getChildren().addAll(createbtn);

      Button deletebtn = new Button("Delete");
      deletebtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            deleteRow(table);
         }
      });      

      VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(10, 0, 0, 10));
      vbox.getChildren().addAll(createlabel,hbox1,hbox2,tablelabel,table,deletebtn);

      return vbox;
   }

   private void deleteRow(TableView table)
   {
      table.getItems().remove(table.getSelectionModel().getSelectedItem());
   }

   private VBox createRuleTable(UIModule... module)
   {
      TableView<Rule> table = new TableView();
      table.setEditable(true);
      TableColumn firstCol = new TableColumn("Head");
      TableColumn lastCol = new TableColumn("Tail");
      firstCol.setCellValueFactory(
         new PropertyValueFactory<Rule, String>("head"));
      lastCol.setCellValueFactory(
         new PropertyValueFactory<Rule, String>("tail"));
      table.getColumns().addAll(firstCol, lastCol);

      Label tablelabel = new Label("Rules");
      tablelabel.setFont(new Font("Arial", 20));
      Label createlabel = new Label("Add Rule");
      createlabel.setFont(new Font("Arial", 20));

      TextField headField = new TextField("head");
      TextField tailField = new TextField("tail");

      ObservableList<Rule> list = FXCollections.observableArrayList();
      table.setItems(list);
      if(module!=null && module.length>0)
      {
         for(Rule a :module[0].getRules())
         {
            list.add(a);
         }
      }

      Button createbtn = new Button("Create Rule");
      createbtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            addRule(list,headField.getText(),tailField.getText());
         }
      });

      Button callbtn = new Button("Add Call");
      callbtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            addCall(tailField, getTabNames());
         }
      });

      HBox hbox1 = new HBox();
      hbox1.setSpacing(5);
      hbox1.setPadding(new Insets(10, 0, 0, 10));
      hbox1.getChildren().addAll(headField,tailField);

      HBox hbox2 = new HBox();
      hbox2.setSpacing(5);
      hbox2.setPadding(new Insets(10, 0, 20, 10));
      hbox2.getChildren().addAll(createbtn, callbtn);

      Button deletebtn = new Button("Delete");
      deletebtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            deleteRow(table);
         }
      });  

      VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(10, 0, 0, 10));
      vbox.getChildren().addAll(createlabel,hbox1,hbox2,tablelabel,table, deletebtn);
      return vbox;
   }

   private void addRule(ObservableList<Rule> table, String head, String tail)
   {
      if(head.isEmpty())
      {
         showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter rule head");
         return;
      }
      table.add(new Rule(head,tail));
   }

   private void addCall(TextField field, List<String> modules)
   {
      final Stage dialog = new Stage();
      dialog.setTitle("New Call");
      dialog.initModality(Modality.APPLICATION_MODAL);
      dialog.initOwner(field.getScene().getWindow());

      final ComboBox moduleMenu = new ComboBox(FXCollections.observableArrayList(modules));

      TextField sentence = new TextField();
      TextField addition = new TextField();
      final ComboBox typemenu = new ComboBox(FXCollections.observableArrayList("Sceptical","Credulous"));
      CheckBox negative = new CheckBox("Call is negative");
      negative.setSelected(false);
      Label error = new Label();
      Button btn = new Button("Add call");
      btn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e) {
            String s = field.getText();
            if(!sentence.getText().isEmpty() && !moduleMenu.getSelectionModel().isEmpty() && !typemenu.getSelectionModel().isEmpty())
            {
               if(!s.isEmpty())
               {
                  s = s + ",";
               }
               s = s + "call{" + moduleMenu.getSelectionModel().getSelectedItem().toString();
               if(!addition.getText().isEmpty())
               {
                  s = s + "+" + addition.getText();
               }
               s = s + ",[";
               String sen = sentence.getText();
               if(negative.isSelected())
               {
                  sen = "not " + sen;
               }
               s = s + sen + "],";
               if(typemenu.getSelectionModel().getSelectedItem().toString().equals("Sceptical"))
               {
                  s = s + "sk}";
               }
               else
               {
                  s = s + "cr}";
               }
               field.setText(s);
               dialog.close();
            }
            else error.setText("Module, Sentence and Type required");
         }
      });

      HBox buttonline = new HBox();
      buttonline.setSpacing(10);
      buttonline.getChildren().addAll(btn,error);
      VBox vbox = new VBox(20);
      vbox.setSpacing(10);
      vbox.setPadding(new Insets(10, 10, 10, 10));
      vbox.getChildren().addAll(new Label("Module"),moduleMenu,new Label("Additional Fact (optional)"),addition,new Label("Sentence"),
         sentence,new Label("Type"),typemenu,negative,buttonline);
      Scene dialogScene = new Scene(vbox, 350, 320);
      dialog.setScene(dialogScene);
      dialog.show();
   }

   private void addAssumption(ObservableList<Assumption> table, String assumption, String contrary)
   {
      if(assumption.isEmpty())
      {
         showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter assumption");
         return;
      }
      table.add(new Assumption(assumption,contrary));
   }

   private String getNewModuleName()
   {
      String name = "Unnamed";

      TextInputDialog dialog = new TextInputDialog();
      dialog.setHeaderText("New Module");
      dialog.setContentText("Please enter new module's name:");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()){
         name = result.get();
      }

      return name;
   }

   private String getNewFrameworkName()
   {
      String name = "Unnamed";

      TextInputDialog dialog = new TextInputDialog();
      dialog.setHeaderText("Save Framework");
      dialog.setContentText("Please enter framework's name:");

      Optional<String> result = dialog.showAndWait();
      if (result.isPresent()){
         name = result.get();
      }

      return name;
   }

   private Menu setupModuleMenu()
   {
      Menu m = new Menu("Modules");
      MenuItem addModule = new MenuItem("Add New Module...");
      addModule.setOnAction(new EventHandler<ActionEvent>()
      {
         public void handle(ActionEvent t)
         {
            addModuleTab(getNewModuleName());
         }
      });
      m.getItems().add(addModule);
      return m;
   }

   private Menu setupFileMenu()
   {
      Menu m = new Menu("File");
      MenuItem save = new MenuItem("Save framework");
      save.setOnAction(new EventHandler<ActionEvent>()
      {
         public void handle(ActionEvent t)
         {
            saveModules(getNewFrameworkName());
         }
      });
      m.getItems().add(save);
      MenuItem load = new MenuItem("Load framework");
      load.setOnAction(new EventHandler<ActionEvent>()
      {
         public void handle(ActionEvent t)
         {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            File file = fileChooser.showOpenDialog(tabPane.getScene().getWindow());
            if(file!=null)
            {
               loadModules(file);
            }
         }
      });
      m.getItems().add(load);
      return m;
   }

   private void addModuleTab(String name, UIModule... module)
   {  
      GridPane pane = new GridPane();
      pane.add(createAssumptionTable(module),0,0);
      pane.add(createRuleTable(module),1,0);

      VBox vbox = new VBox();
      vbox.setSpacing(5);
      vbox.setPadding(new Insets(10, 0, 0, 10));
      vbox.getChildren().addAll(pane,createControlPanel(name));

      HBox hbox = new HBox();
      hbox.setSpacing(5);
      hbox.getChildren().addAll(vbox,createResultsPanel(name));

      Tab tab = new Tab();
      tab.setText(name);
      tab.setContent(hbox);
      tab.setClosable(false);

      tabPane.getTabs().add(tab);
   }

   private HBox createControlPanel(String name)
   {
      HBox hbox = new HBox();
      Button deletebtn = new Button("Delete Module");
      deletebtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e)
         {
            try
            {
               for(Tab t : tabPane.getTabs())
               {
                  if(t.getText().equals(name))
                  {
                     tabPane.getTabs().removeAll(t);
                  }
               }
            }
            catch (Exception ex)
            {
               System.out.println(ex);
            }
         }
      });
      Button compilebtn = new Button("Compile");
      compilebtn.setOnAction(new EventHandler<ActionEvent>() 
      {       
         @Override
         public void handle(ActionEvent e)
         {
            pushModulesToFramework();
            try
            {
               getResults(name);
            }
            catch (Exception ex)
            {
               showAlert(Alert.AlertType.ERROR, "Compiling Error", "Please check framework for errors");
            }
         }
      });
      hbox.getChildren().addAll(deletebtn,compilebtn);
      hbox.setSpacing(5);
      hbox.setPadding(new Insets(10, 10, 10, 10));
      return hbox;
   }

   private VBox createResultsPanel(String name)
   {
      VBox panel = new VBox();
      panel.setSpacing(5);
      panel.setPadding(new Insets(20, 0, 0, 30));
      panel.setMaxHeight(600);
      
      Label titlelabel = new Label("Results");
      titlelabel.setFont(new Font("Arial", 20));
      Label admissiblelabel = new Label("Admissible Sets");
      admissiblelabel.setFont(new Font("Arial", 16));

      ObservableList<String> admissible = FXCollections.observableArrayList();
      ListView<String> admisList = new ListView<String>(admissible);

      Label preferredlabel = new Label("Preferred Sets");
      preferredlabel.setFont(new Font("Arial", 16));

      ObservableList<String> preferred = FXCollections.observableArrayList();
      ListView<String> prefList = new ListView<String>(preferred);

      Label credulouslabel = new Label("Credulous Results");
      credulouslabel.setFont(new Font("Arial", 16));

      ObservableList<String> credulous = FXCollections.observableArrayList();
      ListView<String> credList = new ListView<String>(credulous);

      Label scepticallabel = new Label("Sceptical Results");
      scepticallabel.setFont(new Font("Arial", 16));

      ObservableList<String> sceptical = FXCollections.observableArrayList();
      ListView<String> sceptList = new ListView<String>(sceptical);

      GridPane pane = new GridPane();
      pane.setPadding(new Insets(0, 0, 0, 0));
      pane.add(credulouslabel,0,0);
      pane.add(credList,0,1);
      pane.add(scepticallabel,1,0);
      pane.add(sceptList,1,1);
      
      panel.getChildren().addAll(titlelabel,admissiblelabel,admisList,
         preferredlabel,prefList,pane);
      return panel;
   }

   private void getResults(String name)
   {
      Module m = null;
      ObservableList<List<String>> admissible = getAdmissibleList(name);
      ObservableList<List<String>> preferred = getPreferredList(name);
      ObservableList<String> credulous = getCredulousList(name);
      ObservableList<String> sceptical = getScepticalList(name);
      admissible.clear();
      preferred.clear();
      credulous.clear();
      sceptical.clear();
      try
      {
         m = framework.compileModule(framework.module(name));
         admissible.addAll(m.getAdmissible());
         preferred.addAll(m.getPreferred());
         credulous.addAll(m.getCredulous());
         sceptical.addAll(m.getSceptical());
      }
      catch (Exception e)
      {
         showAlert(Alert.AlertType.ERROR, "Compiling Error", "Please check framework for errors");
      }
      catch (Error e)
      {
         showAlert(Alert.AlertType.ERROR, "Compiling Error", "Please check framework for errors");
      }
   }

   private List<String> getTabNames()
   {
      List<String> names = new ArrayList<String>();
      for(Tab t : tabPane.getTabs())
      {
         names.add(t.getText());
      }
      return names;
   }

   private Tab getTab(String name)
   {
      Tab tab = null;
      for(Tab t : tabPane.getTabs())
      {
         if(t.getText().equals(name))
         {
            tab = t;
         }
      }
      return tab;
   }

   private ObservableList<List<String>> getAdmissibleList(String name)
   {
      Tab t = getTab(name);
      HBox h = (HBox) t.getContent();
      VBox b = (VBox) h.getChildren().get(1);
      ListView view = (ListView) b.getChildren().get(2);
      return view.getItems();
   }

   private ObservableList<List<String>> getPreferredList(String name)
   {
      Tab t = getTab(name);
      HBox h = (HBox) t.getContent();
      VBox b = (VBox) h.getChildren().get(1);
      ListView view = (ListView) b.getChildren().get(4);
      return view.getItems();
   }

   private ObservableList<String> getCredulousList(String name)
   {
      Tab t = getTab(name);
      HBox h = (HBox) t.getContent();
      VBox b = (VBox) h.getChildren().get(1);
      GridPane pane = (GridPane) b.getChildren().get(5);
      ListView view = (ListView) pane.getChildren().get(1);
      return view.getItems();
   }

   private ObservableList<String> getScepticalList(String name)
   {
      Tab t = getTab(name);
      HBox h = (HBox) t.getContent();
      VBox b = (VBox) h.getChildren().get(1);
      GridPane pane = (GridPane) b.getChildren().get(5);
      ListView view = (ListView) pane.getChildren().get(3);
      return view.getItems();
   }

   private void loadModules(File file)
   {
      ObjectInputStream objectIn = null;
      FileInputStream fileIn = null;
      try
      {
         fileIn = new FileInputStream(file);
         objectIn = new ObjectInputStream(fileIn);
         List<UIModule> modules = (List<UIModule>) objectIn.readObject();
         if(modules.size()<1)
         {
            throw new Exception();
         }
         objectIn.close();
         tabPane.getTabs().clear();
         for(UIModule m : modules)
         {
            addModuleTab(m.getName(),m);
         }            
      }
      catch (Exception e)
      {
         e.printStackTrace();
         showAlert(Alert.AlertType.ERROR, "File Error", "Invalid or faulty saved framework");
      } 
   }

   private void saveModules(String name)
   {
      ObjectOutputStream objectOut = null;
      FileOutputStream fileOut = null;
      try
      {
         fileOut = new FileOutputStream("frameworks/"+name+".ser", true);
         objectOut = new ObjectOutputStream(fileOut);
         objectOut.writeObject(serializeFramework());
         objectOut.close();
      } catch (Exception e)
      {
         e.printStackTrace();
         showAlert(Alert.AlertType.ERROR, "File Error", "Error occured while saving");
      } 
   }

   private List<UIModule> serializeFramework()
   {
      List<UIModule> modules = new ArrayList<UIModule>();
      for(Tab t : tabPane.getTabs())
      {
         modules.add(serializeModule(t));
      }
      return modules;
   }

   private UIModule serializeModule(Tab t)
   {
      UIModule m = new UIModule(t.getText());
      HBox h = (HBox) t.getContent();
      VBox b = (VBox) h.getChildren().get(0);
      GridPane g = (GridPane) b.getChildren().get(0);
      VBox v = (VBox) g.getChildren().get(0);
      m.addAssumptions(getTable(v).getItems());
      v = (VBox) g.getChildren().get(1);
      m.addRules(getTable(v).getItems());
      return m;
   }

   private void pushModulesToFramework()
   {
      List<UIModule> modules = serializeFramework();
      for(UIModule m : modules)
      {
         framework.addModString(m.getName(),m.toString());
      }
   }

   private TableView getTable(VBox v)
   {
      for(Object o : v.getChildren())
      {
         if(o instanceof TableView)
         {
            return (TableView) o;
         }
      }
      return null;
   }   

   private void showAlert(Alert.AlertType alertType, String title, String message)
   {
      Alert alert = new Alert(alertType);
      alert.setTitle(title);
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.show();
   }
}
