module recruiter1 {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
    requires javafx.base;
	opens application to javafx.graphics, javafx.fxml,javafx.base;
	opens Model_classes to javafx.graphics, javafx.fxml,javafx.base;
	 exports application;
}
