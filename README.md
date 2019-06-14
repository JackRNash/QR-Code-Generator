# QR-Code-Generator
A QR code generator built from scratch in Java.

![Example QR code](https://i.imgur.com/b1wQGxk.png)
###### An example QR code generated with my program


### Purpose
I love QR codes and was curious how they worked. Unfortunately, I was having a hard time finding a good, technical explanation online
(which naturally only made me more curious). I thought it would be a fun project to try and build one myself from scratch. It's also 
nice practice in project management and turning technical descriptions of things into efficient, scalable, and readable code. I purposefully avoided any descriptions of QR codes that involved programming so I could practice these skills.

### Outline
###### Note: Not all bulletpoints were equally difficult to implement or equally many lines of code
Encoding
- [x] Turn alphanumeric strings into appropriate sequence of numbers
- [x] Converts integers to a fixed length binary representation(and convert those back to ints)
- [x] Implement interleaving of blocks to add support for longer alphanumeric messages
- [x] Fix QR code generation for exceptionally large messages
- [ ] Add support for encoding 8-bit messages

Error Correction
- [x] Create all neccessary helper methods for working in finite field w/ 256 elements(addition, multiplication, polynomial multiplication, polynomial division)
- [x] Create the generator polynomimal(for the error correcting codewords) for an arbitrary degree
- [x] Generate the error correcting code words for a given message

Matrix Generation
- [x] Add the finder patter
- [x] Add the separators
- [x] Add version information
- [x] Add error correction and mask information
- [x] Add timing belt
- [x] Add all alignment patterns for a given version
- [x] Add encoded data to the matrix(i.e. the message that appears when scanned)
- [x] Create simplified method to give message & error correction level which creates the associated QR code in matrix form

Masks
- [x] Apply 1st mask properly
- [x] Add methods for other masks
- [x] Add the tests required to check which mask is best for a given code, and select that mask

GUI
- [x] Given a matrix, draw associated grid of colored squares
- [x] Add the quiet spaces around the QR code
- [x] Add an input GUI
     - [x] Slider for error correction level
     - [x] Input box for message with user-error detection
- [ ] Make GUI much prettier
     
Lookup Tables
- [x] Error correction level and mask information encoded for QR code
- [x] Number of error correcting code words and data code words, as well as blocks(used for interleaving before encoding the message)
- [x] Version information encoded for QR code
- [x] Character capacity for alphanumeric messages for all version and error correction levels
- [x] Alignment pattern coordinates

The lookup tables, it's worth noting, were unfortunately almost all neccessary. The one for encoded version information and the one for encoded error correction & mask level could've been generated with a function, but they were so small and writing the function(as well as testing and debugging it) would've taken long enough that it was quicker to just hard code it. Tragically, the other, *much larger*,  look up tables were all required as there's no formula for generating the numbers in them.

### Notes for future work
- Strange bug with exceptionally large messages(version > 7), haven't investigated thoroughly, but also possible that standard QR readers don't read these large messages well(unlikely). Very far outside typical use case, so not bothering with right now
- Bugs typically come from generating the error correcting code words and weird stuff happening there, look at that quick if there's a bug
- If want to implement case sensitive encoding, must encode 8 bit messages
- Make the GUI look nicer(dealing with GUIs are a pain and it's functional, so I stopped here)

### Resources
I used the following resources as references:
- [Overview of the process, not technical enough though](http://www.swetake.com/qrcode/qr1_en.html)
- [Official QR code website](https://www.qrcode.com/en/)
- [ASCII table](https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html)
- [Gave a mathematical description of the generator function, didn't really look at this paper beyond that b/c it contained a lot of  code](https://www.stthomas.edu/media/collegeofartsandsciences/mathematics/pdf/2017AndersonGroupBarlandCAMReport.pdf)
- [Excellent technical description of the process without mentioning code, my ideal resource and one I used heavily](https://www.thonky.com/qr-code-tutorial/introduction)
- [Image that inspired the project](https://i.redd.it/hcuq4rpn2xs21.png)
