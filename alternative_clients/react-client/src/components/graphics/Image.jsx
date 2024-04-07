import React from 'react'

export const Image = ({image, images}) => {

  File.prototype.convertToBase64 = function (callback) {
    var reader = new FileReader();
    reader.onloadend = function (e) {
      callback(e.target.result, e.target.error);
    };
    reader.readAsDataURL(this);
  }

  return (
    <div>Image</div>
  )
}
