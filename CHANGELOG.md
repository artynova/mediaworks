# Changelog

## 1.0.7

### Fixes

- Fix astral projection sound playing after death unconditionally (reported by miyucomics).

## 1.0.6

- Update Simplified Chinese translation (by ChuijkYahus).

## 1.0.5

### Changes

- Make Locale Magnification and Media Shield enchantments scale beyond the standard max level, in spirit of compatibility with mods that allow overenchanting (suggested by anna-s-h).
- Improve Reciprocation loop behaviour. If the Reciprocation enchantment causes a loop where lack of media causes overcasting damage and overcasting damage causes further reciprocation, the loop now fizzles out after 50 repetitions. This should still kill the player if the loop is capable of doing so in the first place, and in other cases (e.g. some kind of non-invulnerability damage negation) this avoids a crash (crash reported by walksanatora).
- Update Simplified Chinese translation (by ChuijkYahus).


### Fixes

- Fix internal error occurring when trying to etch a Lasting Visage.

## 1.0.4

### Additions

- Media Purification I, a pattern that returns amount of media in an entity.
- Media Purification II, a pattern that returns amount of media in a container at a position.

### Changes

- Remove particles from Macula spells, since they are supposed to be used very often if a macula UI is intended to be
  dynamic (suggested by beholderface).
- Make short-duration visages disappear abruptly instead of with a fadeout (suggested by beholderface).
- Change visage fadeout animation processing to avoid flickering at low server tick rates (suggested by beholderface).

### Fixes

- Fix missing Cardinal Components dependency on fabric (reported by object-Object).
- Fix OptiFine crash (reported by Faded-byte).
- Fix remaining health rendering incorrectly on Forge when in astral projection mode.
- Fix missing Visage type name lang in invalid iota mishap.

## 1.0.3

### Fixes

- Fix Reciprocation triggering hexes only for entity-based damage instead of any damage.

## 1.0.2

### Additions

- Add Simplified Chinese translation (by ChuijkYahus).

### Fixes

- Fix Etch Visage pattern working incorrectly when there are more arguments on the stack below the required ones.
- Potentially fix a forge NPE crash.

## 1.0.1

### Fixes

- Fix crash related to cloak repair ingredients.
- Fix cloak iota tooltips missing.

## 1.0.0

### Additions

- Add the entire project!