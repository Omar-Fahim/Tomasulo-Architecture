# Tomasulo-architecture
Tomasulo architecture simulation using Java
## Structure
- Bus
- Instruction
- Main Class
- Pair (to hold value and tag of the element on the bus)
- RegisterF (Register for floating-point instructions)
- RegisterI (Register for Integer instruction)
- Reservation Station
- Screen
  
The Handled Instructions Are:
### Integer Instructions
* ADDI
* SUBI
* DADD
* BNEZ
### Floating Point Instructions
* ADD.D
* SUB.D
* L.D
* S.D
* MUL.D
* DIV.D
  
The Input Format is :
```
ADDI R2 R2 1
S.D R1 150
BNEZ R3 LOOP
```
The Input is taken from a text file named “program.txt’
The Output is written in a text file name ‘output.txt’
## Assumptions
1. In the writeback stage of the BNEZ, the new correct instruction is issued.
2. When the store in the writeback stage, other instructions may writeback on the bus in the same clock cycle as the store does not write on the bus.
3. SUBI is treated the same as ADDI.
4. All Register Files are filled as follows R[i] = i.
5. Cache is filled as follows M[i] = i.
6. In the writeback stage of a store instruction, a new store instruction may be issued in the same reservation station because we consider the store writeback stage does nothing.
7. When two instructions writeback in the same clock cycle, the priority is as follows:
   - ADD/SUB Instructions
   - MUL/DIV Instructions
   - Load Instructions
   If two instructions of the same type need to writeback in the same clock cycle, we take the one that has the lower tag (A0 before A1 and A1 before A2, and so on...).
