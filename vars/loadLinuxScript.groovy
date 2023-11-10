def call(Map config = [:]) { 
  def scriptcontents = libraryResource "co/sen/scripts/${config.name}"    
  writeFile file: "${config.name}", text: scriptcontents 
  sh "chmod a+x ./${config.name}"
} 