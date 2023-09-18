# Mediaworks

A [Hex Casting](https://github.com/gamma-delta/HexMod) addon with miscellaneous additions and quality-of-life
improvements, aiming to diversify the Hex Casting experience while staying true to the base mod's theme.

## Building notes

- [Curios](https://github.com/TheIllusiveC4/Curios) API crashes the javadoc generation task on forge, apparently due to
  a mismatch between mappings that javadoc can't figure out despite the dependency configuration with remapping. To
  build the main jar despite the javadoc jar failure, build with `--continue`.

## Credits

- The [Hex Casting](https://github.com/gamma-delta/HexMod) mod, of course.
- The [Spirit Walker](https://github.com/BasiqueEvangelist/SpiritWalker) mod. Basic clientside "freecam" functionality
  of Astral Projection is greatly inspired by their implementation, and uses some of their code as a reference.
- The [Perspective](https://github.com/MCLegoMan/Perspective) mod. Astral Projection shader implementation here is
  loosely based on their "grayscale" super secret setting implementation.

