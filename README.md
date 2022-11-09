# Wormholes - Immersive Portals Addon (FORGE)
This is the Forge Variant of the mod If you want FABRIC go Here:
https://github.com/VolmitSoftware/WormholesFabric
--------------------
### This is **Wormholes**

Wormholes is a recreation of the Spigot plugin Wormholes but now using the **ImmersivePortals API  
*Please note that this mod is merely a proof of concept at this stage, but all blocks have crafting recipes and are in the creative tab "wormholes" and the mod is* **intended for Survival usage at the end of the day.**

**Demo Videos:**

### **Quick Start Guide:**
1: Craft/Get the Portal frame blocks.
2: Craft/Get the wand.  
3: Make the portal Frame.  
4: Wand Usage- Right-Click = Set frames. Shift-Right clears the wand Selections, and Sneak-Shift-Right clicking a portal flips the orientations depending on your viewing angle  
5: Click both portals to link  
6: Profit.
--------------------
### **NOTES:**
#### Fabric Notes:
##### You will Need The Immersive Portals Mod and all its Dependencies Here:
[MOD] [https://www.curseforge.com/minecraft/mc-mods/immersive-portals-mod](https://www.curseforge.com/minecraft/mc-mods/immersive-portals-mod)  
[Fabric API] [https://www.curseforge.com/minecraft/mc-mods/fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api)


#### Forge Notes:
##### You will Need The Immersive Portals Mod and all its Dependencies Here:
[MOD] https://www.curseforge.com/minecraft/mc-mods/immersive-portals-for-forge&nbsp;

About the Frames: Please note that this was built with odd frames intended, (1,3,5,7,9 etc...) but work with even scales, they may just need to be remade a few times to get the orientation correct. As the orientation the portal is facing is dependent on YOUR direction to the center of the frame. (So if you are too low it will be below, and if you are too high, it'll be too high and you will wither see no portal on one end or like an upside down square etc...) Breaking the frame, breaks all portals
- The same can happen when trying to find the "middle" of the even blocks, but that should just pose a sleight offset.
- The Frame needs to be complete! (NO MISSING CORNERS)
- Examples for all the points:  
  ![](https://i.imgur.com/Y7fNcle.png)  
  ![](https://i.imgur.com/Dlpjyi1.png)  
  ![](https://i.imgur.com/aaFHjxQ.png)
--------------------
### **How to Avoid Problems:**

- Use "Even Frames"
- Look Directly at your portal (Your facing Direction is important)

- Be on Latest.

### **Need any Help?**

- Join the ImmersivePortals [Discord](https://discord.gg/BZxgURK), (Please note that the Discord is not for **MY** mods, so please dont ask anyone other than me for help.)
- Ask in the comments here. I'll reply eventually

--------------------
Source: [https://github.com/VolmitSoftware/WormholesFML](https://github.com/VolmitSoftware/WormholesFML) (Forge)  
Source: [https://github.com/VolmitSoftware/WormholesFabric](https://github.com/VolmitSoftware/WormholesFabric) (Fabric)  
Downloads/Changelog: [FORGE MOD HERE](https://github.com/VolmitSoftware/WormholesFML/releases/tag/)  
Downloads/Changelog: [FABRIC MOD HERE](https://github.com/VolmitSoftware/WormholesFabric/releases/)



Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access
to some data and functions you need to build a successful mod.

Note also that the patches are built against "un-renamed" MCP source code (aka
SRG Names) - this means that you will not be able to read them directly against
normal code.

Setup Process:
==============================

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: `gradlew genEclipseRuns` (`./gradlew genEclipseRuns` if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder
   or run `gradlew eclipse` to generate the project.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: `gradlew genIntellijRuns` (`./gradlew genIntellijRuns` if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything
{this does not affect your code} and then start the process again.

Mapping Names:
=============================
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license, if you do not agree with it you can change your mapping names to other crowdsourced names in your
build.gradle. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/MinecraftForge/MCPConfig/blob/master/Mojang.md

Additional Resources:
=========================
Community Documentation: http://mcforge.readthedocs.io/en/latest/gettingstarted/  
LexManos' Install Video: https://www.youtube.com/watch?v=8VEdtQLuLO0  
Forge Forum: https://forums.minecraftforge.net/  
Forge Discord: https://discord.gg/UvedJ9m  