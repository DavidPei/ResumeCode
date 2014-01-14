addi $r1 $r1 0x01
addi $r2 $r2 0x02
beq $r0 $r0 two
one: disp $r1 0x00
two: disp $r2 0x00
beq $r0 $r1 two
lui $r3 0xff
bne $r0 $r1 inequal
andi $r1 $r1 0x00
inequal: lui $r3 0x11
