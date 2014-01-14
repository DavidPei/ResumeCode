addi $r2 $r2 0x01
lui $r0 0xff
#set to 1111111100000000
addi $r3 $r3 8
srlv $r0 $r0 $r3
#shift to 0000000011111111
lui $r1 0x7f
or $r0 $r0 $r1
#make it  0111111111111111
add $r0 $r0 $r2
#overflow should occur here going from high pos to most neg
