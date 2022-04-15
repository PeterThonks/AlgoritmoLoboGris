select distinct
     playlist.Name,
    track.Name
from track 
inner join playlisttrack on playlisttrack.TrackId = track.TrackId
inner join playlist on playlist.PlaylistId = playlisttrack.PlaylistId
where track.Milliseconds >= 180 and track.UnitPrice = 0.99;