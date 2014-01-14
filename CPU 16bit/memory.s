addi $r0 $r0 0x01
lui $r1 0x80
sw $r1 0($r0)
lw $r2 0($r0)
sw $r1 1($r0)
lw $r3 1($r0)
andi $r0 $r0 0x00
lui $r0 0xff
andi $r1 $r1 0x00
addi $r1 $r1 0x03
sw $r0 2($r1)
lw $r3 2($r1)
