set key off
set term png
set size square
set output 'images/outer.png'
p 'output/Sun.dat' u 1:3 w l, 'output/Jupiter.dat' u 1:3 w l, 'output/Saturn.dat' u 1:3 w l, 'output/Uranus.dat' u 1:3 w l, 'output/Neptune.dat' u 1:3 w l, 'output/Pluto.dat' u 1:3 w l
