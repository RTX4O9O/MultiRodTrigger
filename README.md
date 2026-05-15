# MultiRodTrigger Plugin
Make fishing rod pull works like unstable smp.
## What does this do
So we all know the fishing bobber bug that make rod pearl stasis chamber a thing, right? This plugin make it more stable. Since the bobber behavior was not intentional, it is considered a bug rather than feature. This explain why it has its annoying limitation, you cannot log off, every rod pulls the same bobber, and loading the chunk with the setup ruins it. But with this plugin, we transit this bug into a feature. Now, you can use multiple rods to bind different pressure plate and pull whichever constraption you set. Furthermore, even you log off or server restart the rod still functions, and loading the chunk with the setup cannot break it.

## How does this work
The plugin listen to every bobber that are landing on a pressure plate. Once landed, the bobber and pressure plate are added to a pending list. If the chunk of bobber gets unloaded while bobber stays on the pressure plate (this can be achieve mostly by entering a portal or tp/tpa to other players), the pending bobber and pressure plate set are migrated from pending list to active list. During this process, the rod player hold is recorded with a unique tag that make the pressure plate only trigger if the specific rod is pulled. The current way we keep pressure plate active is by summoning an invisible, noAi, invulnerable slime on the pressure plate.

## What can I do with this
Any wireless redstone with the rod to pull! Just like how Wemmbu pulls his orbitals and FlameFrags pulls his wolf army!

## How to install
This is a papermc plugin. You need to install a paper server first then download this plugin into your plugin folder.

## Current Issue
### Major
1. Cannot detect chunk unload if there's ender pearl within 3x3 chunk because of entity ticking.
### Minor
1. Using slime on pressure plate is kinda bad.

## This plugin is still in beta stage. Huge welcome if you are willing to help improve this project.
Contact me on discord: _rtx
