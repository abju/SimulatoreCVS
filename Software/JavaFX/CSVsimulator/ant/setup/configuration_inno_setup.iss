; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Simulatore Campane a Sistema Veronese"
#define MyAppPublisher "Marco Dalla Riva"
#define MyAppURL "https://github.com/abju/SimulatoreCVS"
#define CsvcIconFile "C:\Users\lion\Documents\GitHub\SimulatoreCVS\Software\JavaFX\CSVsimulator\resources\images\ico\Bell.ico"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{1836A01A-49B9-497C-8675-22A891B7CD0B}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\SimulatoreCampaneSistemaVeronese
DefaultGroupName=Simulatore Campane Sistema Veronese
AllowNoIcons=yes
OutputDir={#DirOutput}
OutputBaseFilename={#OutputName}
SetupIconFile=C:\Users\lion\Documents\GitHub\SimulatoreCVS\Software\JavaFX\CSVsimulator\resources\images\ico\myBell.ico
Compression=lzma
SolidCompression=yes
ChangesAssociations=yes

[Registry]
Root: HKCR; Subkey: ".csvc"; ValueType: string; ValueName: ""; ValueData: "ConcertoFile"; Flags: uninsdeletevalue
Root: HKCR; Subkey: "ConcertoFile"; ValueType: string; ValueName: ""; ValueData: "ConcertoFile"; Flags: uninsdeletekey
Root: HKCR; Subkey: "ConcertoFile\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\resources\images\ico\bell-c.ico"
Root: HKCR; Subkey: "ConcertoFile\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" --filename ""%1"""

Root: HKCR; Subkey: ".csvs"; ValueType: string; ValueName: ""; ValueData: "SuonataFile"; Flags: uninsdeletevalue
Root: HKCR; Subkey: "SuonataFile"; ValueType: string; ValueName: ""; ValueData: "SuonataFile"; Flags: uninsdeletekey
Root: HKCR; Subkey: "SuonataFile\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\resources\images\ico\bell-s.ico"
Root: HKCR; Subkey: "SuonataFile\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" --filename ""%1"""

[Languages]
Name: "italian"; MessagesFile: "compiler:Languages\Italian.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "C:\Users\lion\Documents\GitHub\SimulatoreCVS\Software\JavaFX\CSVsimulator\ant\setup\results\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: "C:\Users\lion\Documents\GitHub\SimulatoreCVS\Software\JavaFX\CSVsimulator\dist\lib\*"; DestDir: "{app}\lib"; Flags: ignoreversion recursesubdirs createallsubdirs
Source: "C:\Users\lion\Documents\GitHub\SimulatoreCVS\Software\JavaFX\CSVsimulator\resources\images\ico\*"; DestDir: "{app}\resources\images\ico"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

