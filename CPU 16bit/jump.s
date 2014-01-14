addi $r0 $r0 0x01
start: addi $r1 $r1 0x01
addi $r2 $r2 0x01
#j start
#jal start
jr $r1
