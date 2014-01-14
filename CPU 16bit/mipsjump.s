addi $t0 $t0 0x0001
start: addi $ra $ra 0x0001
addi $t2 $t2 0x0001
#j start
#jal start
jr $ra
