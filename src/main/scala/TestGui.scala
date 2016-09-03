package graphics

import scalafx.Includes
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{Label, TextField, TextArea, PasswordField, Button}
import scalafx.scene.paint.Color._
import java.nio.file._
import util.Backup._
import util._
import java.io._

object TestGui {
	/*
	def main(args: Array[String]): Unit = {
		
		
		val app = new JFXApp {
			var pstFiles: List[String] = Nil
			var zippedFiles: List[String] = Nil
			
			stage = new JFXApp.PrimaryStage {
				title = "PST File Zipper"
				scene = new Scene(700, 500) {
					fill = LightBlue
					val label1 = new Label("Backup Location")
					label1.layoutX = 20
					label1.layoutY = 20
					
					val text1 = new TextField
					text1.layoutX = 20
					text1.layoutY = 40
					text1.prefWidth = 400
					text1.promptText = "Enter Backup Location"
					
					val setButton = new Button("Set")
					setButton.layoutX = 425
					setButton.layoutY = 40
					setButton.onAction = handle {
						//val backupFieldText = text1.text
						if(text1.getText != "") {
							label1.text = text1.getText
							text1.text = ""
						}
						
					}
					
					
					val pstFileNameText = new TextField
					pstFileNameText.layoutX = 20
					pstFileNameText.layoutY = 90
					pstFileNameText.prefWidth = 400
					pstFileNameText.promptText = "PST File Location"
					
					val plusButton = new Button("+")
					plusButton.layoutX = 425
					plusButton.layoutY = 90
					plusButton.onAction = handle {
						if(pstFileNameText.getText != "") {

							val pstFile = pstFileNameText.getText
							pstFiles = pstFile :: pstFiles
							filesToZipArea.appendText(pstFile + "\n")
							pstFileNameText.text = ""
						}
					}
					
					val filesToZipArea = new TextArea
					filesToZipArea.layoutX = 20
					filesToZipArea.layoutY = 200
					filesToZipArea.prefHeight = 250
					filesToZipArea.prefWidth = 300
					filesToZipArea.promptText = "Files To Zip"
					
					val zippedFilesArea = new TextArea
					zippedFilesArea.layoutX = 350
					zippedFilesArea.layoutY = 200
					zippedFilesArea.prefHeight = 250
					zippedFilesArea.prefWidth = 300
					zippedFilesArea.promptText = "Zipped Files"
					
					val zipButton = new Button("Zip")
					zipButton.layoutX = 325
					zipButton.layoutY = 470
					zipButton.onAction = handle {
						zippedFilesArea.text = ""
						val stringsToPaths = pstFiles.map(s => Paths.get(s)); println(stringsToPaths)
						val location = Paths.get(label1.getText)
						val fileType = ".pst"
						stringsToPaths.map(p => Backup.safeZip(p, fileType, location))
						zippedFiles = Backup.arrToList(location).map(p => p.toString)
						zippedFiles.map(fs => zippedFilesArea.appendText(fs + "\n"))
					}
					
					
					content = List(label1, text1, pstFileNameText, setButton, plusButton, filesToZipArea, zippedFilesArea, zipButton)
				}
				
			
			}
		
		}
		app.main(args)
		
	}
	*/
}